package com.szusta.meduva.util;

import java.util.Calendar;

public class TimeUtils {

    public static int MINUTE_OFFSET = 30;
    public static int DAYS_RANGE = 30;

    public static Calendar roundToNextHalfHour(Calendar calendar) {
        Calendar toRoundCalendar = (Calendar) calendar.clone();
        int currMinutes = toRoundCalendar.get(Calendar.MINUTE);
        int mod = currMinutes % MINUTE_OFFSET;
        toRoundCalendar.add(Calendar.MINUTE, MINUTE_OFFSET - mod);
        toRoundCalendar.set(Calendar.SECOND, 0);
        toRoundCalendar.set(Calendar.MILLISECOND, 0);
        return toRoundCalendar;
    }

    public static boolean hasThirtyDaysPassedBetween(Calendar now, Calendar someday) {
        Calendar tempSomeday = (Calendar) someday.clone();
        tempSomeday.add(Calendar.DAY_OF_MONTH, -DAYS_RANGE);
        return now.before(tempSomeday);
    }
}
