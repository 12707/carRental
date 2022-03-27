package org.example.controller;

import org.example.job.CarRentalTimerTask;
import org.example.pojo.CommonResponse;
import org.example.service.ICarsRentalService;
import org.example.service.impl.CarsRentalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
public class CarsRentalCtl {
    private Logger logger = Logger.getLogger("CarsRentalCtl");

    private ICarsRentalService carsRentalService;

    private CarRentalTimerTask carRentalTimerTask;

    @Autowired
    public void setCarsRentalService(ICarsRentalService carsRentalService) {
        if (carsRentalService == null) {
            this.carsRentalService = new CarsRentalServiceImpl();
        } else {
            this.carsRentalService = carsRentalService;
        }
    }

    @Autowired
    public void setCarRentalTimerTask(CarRentalTimerTask carRentalTimerTask) {
        if (carRentalTimerTask == null) {
            this.carRentalTimerTask = new AnnotationConfigApplicationContext().getBean(CarRentalTimerTask.class);
        } else {
            this.carRentalTimerTask = carRentalTimerTask;
        }
    }

    @RequestMapping("/rentCar/{customer}/{carModel}/{rentTime}/{unit}")
    public CommonResponse rentCar(@PathVariable("customer") String customer,
                                  @PathVariable("carModel") String carModel,
                                  @PathVariable("rentTime") int rentTime,
                                  @PathVariable("unit") String unit
    ) {
        return carsRentalService.rentCar(customer, carModel, rentTime, unit);
    }

    @RequestMapping("/job/healthCheck")
    public CommonResponse threadHealthCheck() {
        CommonResponse commonResponse = CommonResponse.success();
        commonResponse.setMessage(carRentalTimerTask.healthCheck());
        return commonResponse;
    }

    @RequestMapping("/job/restart")
    public CommonResponse activateSchedule() {
        CommonResponse response;
        if (carRentalTimerTask.shutdownSchedule()) {
            carRentalTimerTask.init();
            response = CommonResponse.success();
        } else {
            response = CommonResponse.fail("Shutdown schedule failed!");
        }
        return response;
    }

    @ExceptionHandler
    @ResponseBody
    public CommonResponse rentCarExceptionHandler(Exception e) {
        return CommonResponse.fail(e.getMessage());
    }
}
