package com.oleksii.simplechat.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public interface DateUtil {

    static String getDateString(Timestamp timestamp, Boolean withLastSeen) {
        String res = withLastSeen ? "last seen " : "";
        Calendar receivedDate = Calendar.getInstance();
        receivedDate.setTime(timestamp);
        Calendar dayBefore = Calendar.getInstance();
        dayBefore.add(Calendar.DATE, -1);
        Calendar twoDaysBefore = Calendar.getInstance();
        twoDaysBefore.add(Calendar.DATE, -2);
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.DATE, -7);

        if (receivedDate.compareTo(dayBefore) >= 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
            res += withLastSeen ? "at " : "";
            res += dateFormat.format(new Date(timestamp.getTime()));
        } else if (receivedDate.compareTo(twoDaysBefore) >= 0) {
            res += withLastSeen ? "yesterday" : getDayOfWeek(receivedDate.get(Calendar.DAY_OF_WEEK));
        } else if (receivedDate.compareTo(weekBefore) >= 0) {
            res += withLastSeen ? "on " : "";
            res += getDayOfWeek(receivedDate.get(Calendar.DAY_OF_WEEK));
        } else {
            res += getShortMonthOfYear(receivedDate.get(Calendar.MONTH))
                    + " " + receivedDate.get(Calendar.DAY_OF_MONTH);
        }

        return res;
    }

    static boolean isNewDay(Timestamp t1, Timestamp t2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(t1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(t2);

        return c1.get(Calendar.YEAR) == c1.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) < c2.get(Calendar.DAY_OF_YEAR);
    }

    static String getTimeString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        return dateFormat.format(new Date(timestamp.getTime()));
    }

    static String getDayMonthString(Timestamp timestamp) {
        Calendar receivedDate = Calendar.getInstance();
        receivedDate.setTime(timestamp);
        return getMonthOfYear(receivedDate.get(Calendar.MONTH)) + " "
                + receivedDate.get(Calendar.DAY_OF_MONTH);
    }

    static String getDayOfWeek(int day) {
        switch (day) {
            case 0:
                return "Mon";
            case 1:
                return "Tue";
            case 2:
                return "Wed";
            case 3:
                return "Thu";
            case 4:
                return "Fri";
            case 5:
                return "Sat";
            case 6:
                return "Sun";
            default:
                return "Error";
        }
    }

    static String getMonthOfYear(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "Error";
        }
    }

    static String getShortMonthOfYear(int month) {
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Error";
        }
    }
}
