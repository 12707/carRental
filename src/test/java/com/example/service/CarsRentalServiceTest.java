package com.example.service;

import org.example.Application;
import org.example.exception.CarsRentalException;
import org.example.pojo.CommonResponse;
import org.example.service.ICarsRentalService;
import org.example.utils.TimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class CarsRentalServiceTest {

    @Autowired
    private ICarsRentalService carsRentalService;

    @Test(expected = CarsRentalException.class)
    public void testRentCarWithEmptyCustomer() {
        carsRentalService.rentCar("", "BMW", 1, TimeUnit.DAY.name());
    }

    @Test(expected = CarsRentalException.class)
    public void testRentCarWithEmptyCarModel() {
        carsRentalService.rentCar("test", "", 1, TimeUnit.DAY.name());
    }

    @Test(expected = CarsRentalException.class)
    public void testRentCarWithArbitraryCarModel() {
        carsRentalService.rentCar("test", "ABC", 1, TimeUnit.DAY.name());
    }

    @Test(expected = CarsRentalException.class)
    public void testRentCarWithNegativeRentTime() {
        carsRentalService.rentCar("test", "Toyota Camry", -1, TimeUnit.DAY.name());
    }

    @Test(expected = CarsRentalException.class)
    public void testRentCarWithWrongUnit() {
        carsRentalService.rentCar("test", "Toyota Camry", 1, "Mismatched");
    }

    @Test
    public void testRentCarByDay() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.DAY.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals(commonResponse.getCode(), "S");
        Assert.assertEquals(commonResponse.getMessage(), "Success");
    }

    @Test
    public void testRentCarBySecond() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.SECOND.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals(commonResponse.getCode(), "S");
        Assert.assertEquals(commonResponse.getMessage(), "Success");
    }

    @Test
    public void testRentCarByMinute() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.MINUTE.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals(commonResponse.getCode(), "S");
        Assert.assertEquals(commonResponse.getMessage(), "Success");
    }

    @Test
    public void testRentCarByHour() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.HOUR.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals(commonResponse.getCode(), "S");
        Assert.assertEquals(commonResponse.getMessage(), "Success");
    }

    @Test
    public void testRentCarByYear() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.YEAR.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals(commonResponse.getCode(), "S");
        Assert.assertEquals(commonResponse.getMessage(), "Success");
    }

    @Test
    public void testRentCarByMonth() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.MONTH.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals(commonResponse.getCode(), "S");
        Assert.assertEquals(commonResponse.getMessage(), "Success");
    }

    @Test
    public void testLockCarRecordWithMatchedName() {
        boolean result = carsRentalService.lockCarRecord("1", "BMW 650");
        Assert.assertTrue(result);
    }

    @Test
    public void testLockCarRecordWithMismatchedName() {
        boolean result = carsRentalService.lockCarRecord("1", "Test BMW 651");
        Assert.assertFalse(result);
    }

    @Test
    public void testUnlockCarRecordsWithEmptyData() {
        int result = carsRentalService.unlockCarRecords();
        Assert.assertEquals(result, 0);
    }

    @Test
    public void testUnlockCarRecordsWithData() throws Exception {
        carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.SECOND.name());
        Thread.sleep(1);
        int result = carsRentalService.unlockCarRecords();
        Assert.assertEquals(result, 1);
    }
}
