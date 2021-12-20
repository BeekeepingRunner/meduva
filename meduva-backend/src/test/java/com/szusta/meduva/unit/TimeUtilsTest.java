package com.szusta.meduva.unit;

import com.szusta.meduva.util.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilsTest {

    Calendar initialDate;
    Calendar dateUnderTest;

    // Sets calendar on 2021.01.01 10:15:00:00
    @BeforeEach
    public void setCalendar() {
        initialDate = Calendar.getInstance();
        initialDate.set(2021, Calendar.JANUARY, 1);
        initialDate.set(Calendar.HOUR_OF_DAY, 10);
        initialDate.set(Calendar.MINUTE, 15);
        initialDate.set(Calendar.SECOND, 0);
        initialDate.set(Calendar.MILLISECOND, 0);

        dateUnderTest = Calendar.getInstance();
    }

    @Test
    @DisplayName("should return same day with time equal to 00:00:00")
    public void dayStartTest() {
        Date potentialDayStart = TimeUtils.getDayStart(initialDate.getTime());
        dateUnderTest.setTime(potentialDayStart);

        assertEquals(initialDate.get(Calendar.YEAR), dateUnderTest.get(Calendar.YEAR));
        assertEquals(initialDate.get(Calendar.MONTH), dateUnderTest.get(Calendar.MONTH));
        assertEquals(initialDate.get(Calendar.DAY_OF_MONTH), dateUnderTest.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, dateUnderTest.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, dateUnderTest.get(Calendar.MINUTE));
        assertEquals(0, dateUnderTest.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return same day with time equal to 23:59:59")
    public void dayEndTest() {
        Date potentialDayEnd = TimeUtils.getDayEnd(initialDate.getTime());
        dateUnderTest.setTime(potentialDayEnd);

        assertEquals(initialDate.get(Calendar.YEAR), dateUnderTest.get(Calendar.YEAR));
        assertEquals(initialDate.get(Calendar.MONTH), dateUnderTest.get(Calendar.MONTH));
        assertEquals(initialDate.get(Calendar.DAY_OF_MONTH), dateUnderTest.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, dateUnderTest.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, dateUnderTest.get(Calendar.MINUTE));
        assertEquals(59, dateUnderTest.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return the next day with time equal to 00:00:00")
    public void nextDayStartTest() {
        Date potentialNextDayStart = TimeUtils.getNextDayStart(initialDate.getTime());
        dateUnderTest.setTime(potentialNextDayStart);

        Calendar nextDay = (Calendar) initialDate.clone();
        nextDay.add(Calendar.DAY_OF_MONTH, 1);

        assertEquals(nextDay.get(Calendar.YEAR), dateUnderTest.get(Calendar.YEAR));
        assertEquals(nextDay.get(Calendar.MONTH), dateUnderTest.get(Calendar.MONTH));
        assertEquals(nextDay.get(Calendar.DAY_OF_MONTH), dateUnderTest.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, dateUnderTest.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, dateUnderTest.get(Calendar.MINUTE));
        assertEquals(0, dateUnderTest.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return the first day of the next month with time equal to 00:00:00")
    public void nextMonthStartTest() {
        Date potentialNextMonthStart = TimeUtils.getNextMonthStart(initialDate.getTime());
        dateUnderTest.setTime(potentialNextMonthStart);

        Calendar nextMonthFirstDay = (Calendar) initialDate.clone();
        nextMonthFirstDay.add(Calendar.MONTH, 1);
        nextMonthFirstDay.set(Calendar.DAY_OF_MONTH, 1);

        assertEquals(nextMonthFirstDay.get(Calendar.YEAR), dateUnderTest.get(Calendar.YEAR));
        assertEquals(nextMonthFirstDay.get(Calendar.MONTH), dateUnderTest.get(Calendar.MONTH));
        assertEquals(nextMonthFirstDay.get(Calendar.DAY_OF_MONTH), dateUnderTest.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, dateUnderTest.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, dateUnderTest.get(Calendar.MINUTE));
        assertEquals(0, dateUnderTest.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return the last day of the same month with time equal to 23:59:59")
    public void monthEndTest() {
        Date potentialMonthEnd = TimeUtils.getMonthEnd(initialDate.getTime());
        dateUnderTest.setTime(potentialMonthEnd);

        Calendar monthEnd = (Calendar) initialDate.clone();
        monthEnd.set(Calendar.DAY_OF_MONTH, initialDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        assertEquals(monthEnd.get(Calendar.YEAR), dateUnderTest.get(Calendar.YEAR));
        assertEquals(monthEnd.get(Calendar.MONTH), dateUnderTest.get(Calendar.MONTH));
        assertEquals(monthEnd.get(Calendar.DAY_OF_MONTH), dateUnderTest.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, dateUnderTest.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, dateUnderTest.get(Calendar.MINUTE));
        assertEquals(59, dateUnderTest.get(Calendar.SECOND));
    }
}
