package com.szusta.meduva.unit;

import com.szusta.meduva.util.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TimeUtilsTest {

    Calendar initialDate;
    Calendar testDate;

    // Sets calendar on 2021.01.01 10:15:00:00
    @BeforeEach
    public void setCalendar() {
        initialDate = Calendar.getInstance();
        initialDate.set(2021, Calendar.JANUARY, 1);
        initialDate.set(Calendar.HOUR_OF_DAY, 10);
        initialDate.set(Calendar.MINUTE, 15);
        initialDate.set(Calendar.SECOND, 0);
        initialDate.set(Calendar.MILLISECOND, 0);

        testDate = Calendar.getInstance();
    }

    @Test
    @DisplayName("should return same day with time equal to 00:00:00")
    public void should_return_same_day_start() {
        Date potentialDayStart = TimeUtils.getDayStart(initialDate.getTime());
        testDate.setTime(potentialDayStart);

        assertEquals(initialDate.get(Calendar.YEAR), testDate.get(Calendar.YEAR));
        assertEquals(initialDate.get(Calendar.MONTH), testDate.get(Calendar.MONTH));
        assertEquals(initialDate.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, testDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, testDate.get(Calendar.MINUTE));
        assertEquals(0, testDate.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return same day with time equal to 23:59:59")
    public void should_return_same_day_end() {
        Date potentialDayEnd = TimeUtils.getDayEnd(initialDate.getTime());
        testDate.setTime(potentialDayEnd);

        assertEquals(initialDate.get(Calendar.YEAR), testDate.get(Calendar.YEAR));
        assertEquals(initialDate.get(Calendar.MONTH), testDate.get(Calendar.MONTH));
        assertEquals(initialDate.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, testDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, testDate.get(Calendar.MINUTE));
        assertEquals(59, testDate.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return the next day with time equal to 00:00:00")
    public void should_return_next_day_start() {
        Date potentialNextDayStart = TimeUtils.getNextDayStart(initialDate.getTime());
        testDate.setTime(potentialNextDayStart);

        Calendar nextDay = (Calendar) initialDate.clone();
        nextDay.add(Calendar.DAY_OF_MONTH, 1);

        assertEquals(nextDay.get(Calendar.YEAR), testDate.get(Calendar.YEAR));
        assertEquals(nextDay.get(Calendar.MONTH), testDate.get(Calendar.MONTH));
        assertEquals(nextDay.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, testDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, testDate.get(Calendar.MINUTE));
        assertEquals(0, testDate.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return the first day of the next month with time equal to 00:00:00")
    public void should_return_next_months_first_day_start() {
        Date potentialNextMonthStart = TimeUtils.getNextMonthStart(initialDate.getTime());
        testDate.setTime(potentialNextMonthStart);

        Calendar nextMonthFirstDay = (Calendar) initialDate.clone();
        nextMonthFirstDay.add(Calendar.MONTH, 1);
        nextMonthFirstDay.set(Calendar.DAY_OF_MONTH, 1);

        assertEquals(nextMonthFirstDay.get(Calendar.YEAR), testDate.get(Calendar.YEAR));
        assertEquals(nextMonthFirstDay.get(Calendar.MONTH), testDate.get(Calendar.MONTH));
        assertEquals(nextMonthFirstDay.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, testDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, testDate.get(Calendar.MINUTE));
        assertEquals(0, testDate.get(Calendar.SECOND));
    }

    @Test
    @DisplayName("should return the last day of the same month with time equal to 23:59:59")
    public void should_return_same_months_last_day_end() {
        Date potentialMonthEnd = TimeUtils.getMonthEnd(initialDate.getTime());
        testDate.setTime(potentialMonthEnd);

        Calendar monthEnd = (Calendar) initialDate.clone();
        monthEnd.set(Calendar.DAY_OF_MONTH, initialDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        assertEquals(monthEnd.get(Calendar.YEAR), testDate.get(Calendar.YEAR));
        assertEquals(monthEnd.get(Calendar.MONTH), testDate.get(Calendar.MONTH));
        assertEquals(monthEnd.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, testDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, testDate.get(Calendar.MINUTE));
        assertEquals(59, testDate.get(Calendar.SECOND));
    }

    @Nested
    class nDaysElapsed {

        @Test
        @DisplayName("Should return true only if the actual number of elapsed days is equal or greater from the specified")
        public void if_n_days_elapsed_then_true() {
            testDate = (Calendar) initialDate.clone();
            int daysElapsed = 5;
            testDate.add(Calendar.DAY_OF_MONTH, daysElapsed);

            assertTrue(TimeUtils.hasNDaysElapsed(initialDate, testDate, daysElapsed));
            assertTrue(TimeUtils.hasNDaysElapsed(initialDate, testDate, daysElapsed - 1));
            assertFalse(TimeUtils.hasNDaysElapsed(initialDate, testDate, daysElapsed + 1));
        }

        @Test
        @DisplayName("Should take hourly time into consideration")
        public void should_behave_correct_if_day_time_differs_sligthly() {
            int daysElapsed = 5;

            Calendar momentAfter = (Calendar) initialDate.clone();
            momentAfter.add(Calendar.DAY_OF_MONTH, daysElapsed);
            momentAfter.add(Calendar.SECOND, 1);

            assertTrue(TimeUtils.hasNDaysElapsed(initialDate, momentAfter, daysElapsed));

            Calendar momentBefore = (Calendar) initialDate.clone();
            momentBefore.add(Calendar.DAY_OF_MONTH, daysElapsed);
            momentBefore.add(Calendar.SECOND, -1);

            assertFalse(TimeUtils.hasNDaysElapsed(initialDate, momentBefore, daysElapsed));
        }

        @Test
        @DisplayName("Should work on dates with different months")
        public void hasNDaysElapsedTest3() {
            testDate = (Calendar) initialDate.clone();
            int daysElapsed = 45;
            testDate.add(Calendar.DAY_OF_MONTH, daysElapsed);

            assertTrue(TimeUtils.hasNDaysElapsed(initialDate, testDate, daysElapsed));
        }

        @Test
        @DisplayName("Should work on dates with different years")
        public void hasNDaysElapsedTest4() {
            testDate = (Calendar) initialDate.clone();
            int daysElapsed = 500;
            testDate.add(Calendar.DAY_OF_MONTH, daysElapsed);

            assertTrue(TimeUtils.hasNDaysElapsed(initialDate, testDate, daysElapsed));
        }
    }
}
