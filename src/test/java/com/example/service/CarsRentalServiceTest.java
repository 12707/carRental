package com.example.service;

import org.example.Application;
import org.example.exception.CarsRentalException;
import org.example.pojo.CommonResponse;
import org.example.service.ICarsRentalService;
import org.example.utils.CommonTools;
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
    public void testRentCarBySecond() {
        carsRentalService.unlockCarRecords(CommonTools.parse("20990101"));
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.SECOND.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals("S", commonResponse.getCode());
        Assert.assertEquals("Success", commonResponse.getMessage());
    }

    @Test
    public void testRentCarByDay() {
        carsRentalService.unlockCarRecords(CommonTools.parse("20990101"));
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.DAY.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals("S", commonResponse.getCode());
        Assert.assertEquals("Success", commonResponse.getMessage());
    }

    @Test
    public void testRentCarByMinute() {
        carsRentalService.unlockCarRecords(CommonTools.parse("20990101"));
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.MINUTE.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals("S", commonResponse.getCode());
        Assert.assertEquals("Success", commonResponse.getMessage());
    }

    @Test
    public void testRentCarByHour() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.HOUR.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals("S", commonResponse.getCode());
        Assert.assertEquals("Success", commonResponse.getMessage());
    }

    @Test
    public void testRentCarByYear() {
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.YEAR.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals("S", commonResponse.getCode());
        Assert.assertEquals("Success", commonResponse.getMessage());
    }

    @Test
    public void testRentCarByMonth() {
        carsRentalService.unlockCarRecords(CommonTools.parse("20990101"));
        CommonResponse commonResponse = carsRentalService.rentCar("test", "Toyota Camry", 1, TimeUnit.MONTH.name());
        Assert.assertNotNull(commonResponse);
        Assert.assertEquals("S", commonResponse.getCode());
        Assert.assertEquals("Success", commonResponse.getMessage());
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
    public void testUnlockCarRecordsWithAEmptyData() {
        int result = carsRentalService.unlockCarRecords();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testUnlockCarRecordsWithData() throws Exception {
        carsRentalService.rentCar("test", "BMW 650", 1, TimeUnit.SECOND.name());
        Thread.sleep(1000);
        int result = carsRentalService.unlockCarRecords();
        Assert.assertEquals(1, result);
    }
}
