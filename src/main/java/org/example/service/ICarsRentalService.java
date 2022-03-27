package org.example.service;

import org.example.dto.CarInventoryDto;
import org.example.pojo.CommonResponse;

import java.util.List;

public interface ICarsRentalService {
    /**
     * Let customer reverse a car for a period of time.
     *
     * @param customer
     * @param carModel
     * @param rentTime
     * @param unit
     * @return
     */
    CommonResponse rentCar(String customer, String carModel, int rentTime, String unit);

    /**
     * It is used to lock a record of car by unique uuid that is generated by system.
     *
     * @param uuid
     * @return
     */
    boolean lockCarRecord(String uuid, String carModel);

    /**
     * Find out all records that are already overdue, there are several steps as below:
     * 1, migrate these data to history-related table.
     * 2, remove these data from the formal table.
     * 3, update the inventory table, make related data available to rent.
     *
     * @return
     */
    int unlockCarRecords();

    /**
     * find car's count in stock, it is used for fronted page.
     *
     * @return
     */
    List<CarInventoryDto> findInStockCount();
}
