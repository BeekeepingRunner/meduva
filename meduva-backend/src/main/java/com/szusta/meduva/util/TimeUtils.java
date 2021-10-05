package com.szusta.meduva.util;

import java.util.Calendar;

public class TimeUtils {

    public static int MINUTE_OFFSET = 30;

    public static Calendar roundToNextHalfHour(Calendar calendar) {
        Calendar toRoundCalendar = (Calendar) calendar.clone();
        int currMinutes = toRoundCalendar.get(Calendar.MINUTE);
        int mod = currMinutes % MINUTE_OFFSET;
        toRoundCalendar.add(Calendar.MINUTE, MINUTE_OFFSET - mod);
        toRoundCalendar.set(Calendar.SECOND, 0);
        toRoundCalendar.set(Calendar.MILLISECOND, 0);
        return toRoundCalendar;
    }

    public static boolean hasNDaysPassedBetween(Calendar now, Calendar someday, int days) {
        Calendar tempSomeday = (Calendar) someday.clone();
        tempSomeday.add(Calendar.DAY_OF_MONTH, -days);
        return now.before(tempSomeday);
    }
}
