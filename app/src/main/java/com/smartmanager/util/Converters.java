package com.smartmanager.util;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


/**
 * Convenience methods for data parsing.
 */
public class Converters {

    //region Converters for Room
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    //endregion

    /**
     * @param dateTimeString jodaDateTime in a string format
     * @return Two strings that represent date and time
     */
    public static String[] jodaDateTimeToStringArray(String dateTimeString) {
        StringBuilder dateSb = new StringBuilder();
        String[] dateTimeValues = dateTimeString.split(" ");
        String monthName = dateTimeValues[1];
        Date date = null;
        try {
            date = new SimpleDateFormat("MMM").parse(monthName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        int monthNumber = cal.get(Calendar.MONTH) + 1;

        dateSb.append(dateTimeValues[2]).append("/");
        dateSb.append(monthNumber).append("/");
        dateSb.append(dateTimeValues[5]);
        String formattedDateString = dateSb.toString();

        String[] timeValues = dateTimeValues[3].split(":");
        int hours = Integer.parseInt(timeValues[0]);
        int minutes = Integer.parseInt(timeValues[1]);
        StringBuilder timeSb = new StringBuilder();
        if (hours < 10) {
            timeSb.append(0);
        }
        timeSb.append(hours);
        timeSb.append(":");
        if (minutes < 10) {
            timeSb.append(0);
        }
        timeSb.append(minutes);
        String formattedTimeString = timeSb.toString();

        return new String[]{formattedDateString, formattedTimeString};
    }

    public static String jodaDateTimeToString(String[] dateTimeStringArray) {
        return Arrays.toString(dateTimeStringArray);
    }
}
