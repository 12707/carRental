package org.example.service.impl;

import org.example.dao.CarInventoryRepository;
import org.example.dao.CarRentalHisRepository;
import org.example.dao.CarRentalRepository;
import org.example.domain.CarInventory;
import org.example.domain.CarRental;
import org.example.domain.CarRentalHis;
import org.example.dto.CarInventoryDto;
import org.example.dto.CarRentalDto;
import org.example.dto.CarRentalHisDto;
import org.example.exception.CarsRentalException;
import org.example.pojo.CommonResponse;
import org.example.service.ICarsRentalService;
import org.example.utils.CommonTools;
import org.example.utils.TimeUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.example.utils.Errors.*;

@Service
public class CarsRentalServiceImpl implements ICarsRentalService {
    private Logger logger = Logger.getLogger("CarsRentalServiceImpl");

    @Autowired
    private CarInventoryRepository carInventoryRepository;

    @Autowired
    private CarRentalRepository carRentalRepository;

    @Autowired
    private CarRentalHisRepository carRentalHisRepository;

    @Override
    @Transactional
    public CommonResponse rentCar(String customer, String carModel, int rentTime, String unit) {
        CommonResponse response;
        validateParams(customer, carModel, rentTime, unit);
        String uuid = CommonTools.generateUuid();
        if (lockCarRecord(uuid, carModel)) {
            boolean result = handleRentalProcess(customer, uuid, null, rentTime, unit);
            if (result) {
                response = CommonResponse.success();
            } else {
                throw new CarsRentalException(SAVE_RENTAL_EXCEPTION);
            }
        } else {
            throw new CarsRentalException(LOCK_RECORD_FAIL);
        }
        return response;
    }

    @Override
    @Transactional(value = REQUIRED)
    public boolean lockCarRecord(String uuid, String carModel) {
        int result = carInventoryRepository.lockCarForBooking(uuid, carModel);
        return result > 0;
    }

    @Override
    @Transactional
    public int unlockCarRecords() {
        return unlockCarRecords(new Date());
    }

    @Override
    @Transactional
    public int unlockCarRecords(Date date) {
        int result = 0;
        try {
            List<CarRental> carRentalList = carRentalRepository.findOverDueRentCars(date);
            List<BigInteger> carIdParams = new ArrayList<>();
            List<BigInteger> rentIdParams = new ArrayList<>();
            if (carRentalList != null && carRentalList.size() > 0) {
                carIdParams = carRentalList.stream().map(CarRental::getCarId).collect(Collectors.toList());
                rentIdParams = carRentalList.stream().map(CarRental::getId).collect(Collectors.toList());
                List<CarRentalHis> carRentalHisList = new ArrayList<>();
                for (CarRental carRental : carRentalList) {
                    CarRentalHis carRentalHis = new CarRentalHis();
                    BeanUtils.copyProperties(carRental, carRentalHis);
                    carRentalHisList.add(carRentalHis);
                }
                carRentalHisRepository.saveAll(carRentalHisList);
            }

            if (carIdParams.size() > 0) {
                result = carInventoryRepository.restoreCarsToRent(carIdParams);
            }

            if (rentIdParams.size() > 0) {
                //carRentalRepository.deleteAllById(rentIdParams);
                carRentalRepository.deleteAll(carRentalList);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception happens in unlockCarRecords, ex=" + e.getMessage());
        }

        return result;
    }

    @Override
    public List<CarInventoryDto> findInStockCount() {
        List<?> list = carInventoryRepository.findInStockCount();
        List<CarInventoryDto> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                CarInventoryDto carInventoryDto = new CarInventoryDto();
                if (obj instanceof Object[]) {
                    Object[] carInventory = (Object[])obj;
                    carInventoryDto.setName(carInventory[0].toString());
                    carInventoryDto.setCount(Integer.parseInt(carInventory[1].toString()));
                }
                result.add(carInventoryDto);
            }
        }
        return result;
    }

    @Override
    public List<CarRentalDto> findAllRentCars() {
        List<CarRental> carRentalList = carRentalRepository.findRentCars(new Date());
        List<CarRentalDto> result = new ArrayList<>();
        if (carRentalList != null && carRentalList.size() > 0) {
            for (CarRental carRental : carRentalList) {
                CarRentalDto carRentalDto = new CarRentalDto();
                carRentalDto.setCarId(String.valueOf(carRental.getCarId()));
                carRentalDto.setCustomer(carRental.getCustomer());
                carRentalDto.setCarModel(carRental.getCarModel());
                carRentalDto.setStartDate(CommonTools.formatDate(carRental.getStartDate()));
                carRentalDto.setDueDate(CommonTools.formatDate(carRental.getDueDate()));
                result.add(carRentalDto);
            }
        }
        return result;
    }

    @Override
    public List<CarRentalHisDto> findAllHisRentCars() {
        List<CarRentalHis> carRentalHisList = carRentalHisRepository.findAll();
        List<CarRentalHisDto> result = new ArrayList<>();
        if (carRentalHisList != null && carRentalHisList.size() > 0) {
            for (CarRentalHis carRental : carRentalHisList) {
                CarRentalHisDto carRentalDto = new CarRentalHisDto();
                carRentalDto.setCarId(String.valueOf(carRental.getCarId()));
                carRentalDto.setCustomer(carRental.getCustomer());
                carRentalDto.setCarModel(carRental.getCarModel());
                carRentalDto.setStartDate(CommonTools.formatDate(carRental.getStartDate()));
                carRentalDto.setDueDate(CommonTools.formatDate(carRental.getDueDate()));
                result.add(carRentalDto);
            }
        }
        return result;
    }

    /**
     * check if the input parameters match business requirements.
     *
     * @param customer
     * @param carModel
     * @param rentTime
     * @param unit
     */
    private void validateParams(String customer, String carModel, int rentTime, String unit) {
        if (!StringUtils.hasText(customer)) {
            throw new CarsRentalException(ILLEGAL_CUSTOMER);
        }

        if (!StringUtils.hasText(carModel)) {
            throw new CarsRentalException(INVALID_INPUT, carModel);
        }

        if (rentTime <= 0) {
            throw new CarsRentalException(POSITIVE_INTEGER_REQUIRED, String.valueOf(rentTime));
        }

        boolean flag = false;
        for (TimeUnit timeUnit : TimeUnit.values()) {
            if (timeUnit.name().equalsIgnoreCase(unit)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new CarsRentalException(TIME_UNIT_EXCEPTION, unit);
        }
    }

    /**
     * save rental info to a formal table.
     *
     * @param customer
     * @param uuid
     * @param rentStartDate
     * @param rentTime
     * @param unit
     * @return
     */
    private boolean handleRentalProcess(String customer, String uuid, String rentStartDate, int rentTime, String unit) {
        boolean result = false;
        CarInventory carInventory = carInventoryRepository.findByLockId(uuid);
        if (carInventory != null) {
            CarRental carRental = new CarRental();
            carRental.setCustomer(customer);
            carRental.setCarId(carInventory.getId());
            carRental.setCarModel(carInventory.getName());
            Date date = getRentStartDate(rentStartDate);
            carRental.setStartDate(date);
            carRental.setDueDate(calcRentDueDate(date, rentTime, unit));
            carRentalRepository.save(carRental);
            result = true;
        }
        return result;
    }

    /**
     * get the date that a customer booked a car, it defaults to current date if input date is null.
     *
     * @param rentStartDate
     * @return
     */
    private Date getRentStartDate(String rentStartDate) {
        Date result = null;
        if (StringUtils.hasText(rentStartDate)) {
            result = CommonTools.parse(rentStartDate);
        }

        if (result == null) {
            return new Date();
        }
        return result;
    }

    /**
     * calculate the due date that a customer booked a car based on the start date and a period of time customer inputs.
     *
     * @param startDate
     * @param rentTime
     * @param unit
     * @return
     */
    private Date calcRentDueDate(Date startDate, int rentTime, String unit) {
        return CommonTools.parse(startDate, rentTime, unit);
    }
//
//    private boolean isLegalDate(String strDate) {
//        boolean result = false;
//        try {
//            result = CommonTools.parse(strDate).after(new Date());
//        } catch (Exception e) {
//            logger.log(Level.ALL, "Checking input date exception, ex=" + e.getMessage());
//        }
//        return result;
//    }
}
