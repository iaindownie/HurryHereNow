package com.spawny.HHNbeta;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by iaindownie on 14/12/2015.
 */
public class Utils {

    /**
     * Snaffled from
     * http://www.technotalkative.com/android-asynchronous-image-loading-in-listview/
     * @param is
     * @param os
     */
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Method to calculate the number of remaining days for an offer
     * @param endDate - A String data from the SERVER data download
     * @return - a count of number of days left
     */
    public static int getDaysRemaining(String endDate){
        Calendar now = Calendar.getInstance();
        Calendar end = convertToCalendar(endDate);
        long diff = end.getTime().getTime() - now.getTime().getTime();
        Long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays.intValue()+1;
    }

    /**
     * Converts String date to Calendar: Based on example: "2015-12-21 15:39:00"
     * @param date - A string date
     * @return - Calendar set to date in String
     */
    public static Calendar convertToCalendar(String date){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        cal.clear();
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

}