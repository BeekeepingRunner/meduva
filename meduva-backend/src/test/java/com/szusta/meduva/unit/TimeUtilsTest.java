package com.szusta.meduva.unit;

import com.szusta.meduva.util.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilsTest {

    Date preparedSampleDate;
    Calendar comparativeCalendar;

    // Sets calendar on 2021.01.01 10:15:00:00
    @BeforeEach
    public void setCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.JANUARY, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        preparedSampleDate = calendar.getTime();

        comparativeCalendar = Calendar.getInstance();
    }

    @Test
    @DisplayName("time of given day should be 00:00:00")
    public void dayStartTest() {
        Date potentialDayStart = TimeUtils.getDayStart(preparedSampleDate);
        comparativeCalendar.setTime(potentialDayStart);
        assertEquals(0, comparativeCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, comparativeCalendar.get(Calendar.MINUTE));
        assertEquals(0, comparativeCalendar.get(Calendar.SECOND));
    }
}
