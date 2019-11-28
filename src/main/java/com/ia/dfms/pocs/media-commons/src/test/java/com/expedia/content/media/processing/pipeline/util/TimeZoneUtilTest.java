package com.expedia.content.media.processing.pipeline.util;

import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneUtilTest {

    @Test
    public void testLcmTimeZoneConversion() throws ParseException {
        String inputTime = "2016-04-08 13:39:00";
        Date convertDate = TimeZoneUtil.convertLCMTimeZoneDate(inputTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testTimeZoneConversionCommon() throws ParseException {
        String inputTime = "2016-04-08 13:39:00";
        String expectedPSTTime = "2016-04-08 13:39:00";
        TimeZone asiaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        Date convertDate = TimeZoneUtil.convertTimeZoneDate(inputTime, asiaTimeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertNotEquals(expectedPSTTime, dateFormat.format(convertDate));

    }
}
