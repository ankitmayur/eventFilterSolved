package com.mariner.xvu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the Date util for Date Conversion
 *
 * @author  Ankit Mayur
 * @version 1.0
 * @since   2019-08-31
 */
public class DateUtil {

    /**
     * This method is used convert epoch time to UTC time
     *
     * @param timeInUnixSeconds time in Unix second(epoch)
     * @return Time in UTC converted to string
     */
    public static String getTimeStampFromEpoch(String timeInUnixSeconds) {
        String result = timeInUnixSeconds;
        Long unixTimeLong = 0L;
        try {
            unixTimeLong = Long.parseLong(timeInUnixSeconds);
        } catch (NumberFormatException n) {
            n.printStackTrace();
        }
        Date date = new java.util.Date(unixTimeLong);
        // the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * This method is used convert epoch time to UTC time
     *
     * @param dateInADT date in ADT Timezone
     * @return date in UTC Timezone converted to string
     */
    public static String convertADTToUTC(String dateInADT) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date dateADT = new Date();
        try {
            dateADT = sdf.parse(dateInADT);
            // give a timezone reference for formatting (see comment at the bottom)
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        } catch (ParseException p) {
            p.printStackTrace();
        }
        return sdf.format(dateADT);
    }

}
