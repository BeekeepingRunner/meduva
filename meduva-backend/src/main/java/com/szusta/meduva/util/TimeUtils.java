package com.szusta.meduva.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static int MINUTE_OFFSET = 30;

    /**
     *  Returns the same date with time equal to 00:00:00
     */
    public static Date getDayStart(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     *  Returns the same date with time equal to 23:59:59
     */
    public static Date getDayEnd(Date dateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     *  Returns the first day of month with time equal to 00:00:00
     */
    public static Date getMonthStart(Date anyDayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(anyDayOfMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStart(calendar.getTime());
    }

    /**
     *  Returns the next day with time equal to 00:00:00
     */
    public static Date getNextDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return getDayStart(calendar.getTime());
    }

    /**
     *  Returns the first day of the next month with time equal to 00:00:00
     */
    public static Date getNextMonthStart(Date anyDayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(anyDayOfMonth);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDayStart(calendar.getTime());
    }

    /**
     *  Returns the last day of month with time equal to 23:59:59
     */
    public static Date getMonthEnd(Date anyDayOfMonth) {
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

    public static Calendar roundToNextHalfHour(Calendar calendar) {
        Calendar toRoundCalendar = (Calendar) calendar.clone();
        int currMinutes = toRoundCalendar.get(Calendar.MINUTE);
        int mod = currMinutes % MINUTE_OFFSET;
        toRoundCalendar.add(Calendar.MINUTE, MINUTE_OFFSET - mod);
        toRoundCalendar.set(Calendar.SECOND, 0);
        toRoundCalendar.set(Calendar.MILLISECOND, 0);
        return toRoundCalendar;
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
