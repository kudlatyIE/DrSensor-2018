package ie.droidfactory.drsensor.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kudlaty on 2018-01-20.
 * misc utils methods
 */

public class DataUtils {

    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Convert timestamp into date format string
     * @param timestamp long in miliseconds
     * @return string in full date format
     */
    private static String getDate(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        return DateFormat.format(DATE_FORMAT, cal).toString();
    }
}
