package com.expedia.content.media.processing.pipeline.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * this class is used to set time zone information when get date from LCM database.
 */
public final class TimeZoneUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final TimeZone LCM_TIME_ZONE = TimeZone.getTimeZone("America/Los_Angeles");

    private TimeZoneUtil() {
    }

    /**
     * set the LCM time zone to the date.
     *
     * @param dateAsString date string format like '2016-04-08 13:39:00'
     * @return java.util.Date
     * throws ParseException when the string format is not right
     */
    public static Date convertLCMTimeZoneDate(String dateAsString) throws ParseException {
        DATE_FORMAT.setTimeZone(LCM_TIME_ZONE);
        return DATE_FORMAT.parse(dateAsString);
    }

    /**
     * set the specific timezone to a date from parameter
     *
     * @param dateAsString   date string format like '2016-04-08 13:39:00'
     * @param commonTimeZone any time zone
     * @return java.util.Date
     * throws ParseException when the string format is not right
     */
    public static Date convertTimeZoneDate(String dateAsString, TimeZone commonTimeZone) throws ParseException {
        DATE_FORMAT.setTimeZone(commonTimeZone);
        return DATE_FORMAT.parse(dateAsString);

    }
}
