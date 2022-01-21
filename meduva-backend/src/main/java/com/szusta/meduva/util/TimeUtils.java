package com.szusta.meduva.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    /**
     *  Returns the same date with time equal to 00:00:00
     */
    public static Calendar getDayStart(Date dateTime) {
        Calendar dayStart = Calendar.getInstance();
        dayStart.setTime(dateTime);
        dayStart.set(Calendar.HOUR_OF_DAY, 0);
        dayStart.set(Calendar.MINUTE, 0);
        dayStart.set(Calendar.SECOND, 0);
        return dayStart;
    }

    /**
     *  Returns the same date with time equal to 23:59:59
     */
    public static Calendar getDayEnd(Date dateTime) {
        Calendar dayEnd = Calendar.getInstance();
        dayEnd.setTime(dateTime);
        dayEnd.set(Calendar.HOUR_OF_DAY, 23);
        dayEnd.set(Calendar.MINUTE, 59);
        dayEnd.set(Calendar.SECOND, 59);
        return dayEnd;
    }

    /**
     *  Returns the first day of month with time equal to 00:00:00
     */
    public static Calendar getMonthStart(Date anyDayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(anyDayOfMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStart(calendar.getTime());
    }

    /**
     *  Returns the next day with time equal to 00:00:00
     */
    public static Calendar getNextDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return getDayStart(calendar.getTime());
    }

    /**
     *  Returns the first day of the next month with time equal to 00:00:00
     */
    public static Calendar getNextMonthStart(Date anyDayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(anyDayOfMonth);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStart(calendar.getTime());
    }

    /**
     *  Returns the last day of month with time equal to 23:59:59
     */
    public static Calendar getMonthEnd(Date anyDayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(anyDayOfMonth);
        int maxDayNumber =  calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDayNumber);
        return getDayEnd(calendar.getTime());
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static boolean hasNDaysElapsed(Calendar start, Calendar end, int days) {
        Calendar nDaysBeforeEnd = (Calendar) end.clone();
        nDaysBeforeEnd.add(Calendar.DAY_OF_MONTH, -days);
        return start.before(nDaysBeforeEnd)
                || areAtTheSameTime(start, nDaysBeforeEnd);
    }

    private static boolean areAtTheSameTime(Calendar cal1, Calendar cal2) {
        return !cal1.before(cal2) && !cal1.after(cal2);
    }
}
