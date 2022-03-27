package com.example.utils;

import org.example.utils.CommonTools;
import org.example.utils.TimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonTools.class})
public class CommonToolsTest {
    @Test
    public void testGenerateUuid() {
        String uuid = CommonTools.generateUuid();
        Assert.assertNotNull(uuid);
        Assert.assertEquals(uuid, 36, uuid.length());
    }

    @Test
    public void testParse() {
        Date date = CommonTools.parse("20220326");
        Assert.assertNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 26, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date2 = calendar.getTime();
        Assert.assertEquals(date.getTime(), date2.getTime());
    }

    @Test
    public void testParseWithOtherFormatDate() {
        Date date = CommonTools.parse("2022-03-26");
        Assert.assertNull(date);
    }

    @Test
    public void testParseWithWrongFormatDate() {
        Date date = CommonTools.parse("ABCDE");
        Assert.assertNull(date);
    }

    @Test
    public void testParseWith2Parameters() {
        Date date = CommonTools.parse(CommonTools.parse("20220326"), 2, TimeUnit.DAY.name());
        Assert.assertNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 28, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date2 = calendar.getTime();
        Assert.assertEquals(date.getTime(), date2.getTime());
    }

    @Test
    public void testParseWith2ParametersNegativeNumber() {
        Date date = CommonTools.parse(CommonTools.parse("20220326"), -2, TimeUnit.DAY.name());
        Assert.assertNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 2, 24, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date2 = calendar.getTime();
        Assert.assertEquals(date.getTime(), date2.getTime());
    }
}
