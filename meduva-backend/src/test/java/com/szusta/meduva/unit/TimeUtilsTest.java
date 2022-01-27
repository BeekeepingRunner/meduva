package com.szusta.meduva.unit;

import com.szusta.meduva.util.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class TimeUtilsTest {

    Calendar initialSampleDate;
    Calendar testDate;

    /**
     * Sets helper calendar on 2021.01.01 10:15:00:00
     */
    @BeforeEach
    public void setCalendar() {
        initialSampleDate = Calendar.getInstance();
        initialSampleDate.set(2021, Calendar.JANUARY, 1);
        initialSampleDate.set(Calendar.HOUR_OF_DAY, 10);
        initialSampleDate.set(Calendar.MINUTE, 15);
        initialSampleDate.set(Calendar.SECOND, 0);
        initialSampleDate.set(Calendar.MILLISECOND, 0);

        testDate = Calendar.getInstance();
    }

    @Nested
    class getDayStartTest {
        @Test
        @DisplayName("should return the same day as the input, with time equal to 00:00:00")
        public void should_returnSameDayAtMidnight() {
            // given
            testDate.setTime(initialSampleDate.getTime());

            // when
            testDate = TimeUtils.getDayStart(testDate.getTime());

            // then
            assertAll(
                    () -> assertEquals(initialSampleDate.get(Calendar.YEAR), testDate.get(Calendar.YEAR)),
                    () -> assertEquals(initialSampleDate.get(Calendar.MONTH), testDate.get(Calendar.MONTH)),
                    () -> assertEquals(initialSampleDate.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(0, testDate.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(0, testDate.get(Calendar.MINUTE)),
                    () -> assertEquals(0, testDate.get(Calendar.SECOND))
            );
        }
    }


    @Nested
    class getDayEndTest {
        @Test
        @DisplayName("should return same day with time equal to 23:59:59")
        public void should_return_same_day_end() {
            // given
            testDate.setTime(initialSampleDate.getTime());

            // when
            testDate = TimeUtils.getDayEnd(testDate.getTime());

            // then
            assertAll(
                    () -> assertEquals(initialSampleDate.get(Calendar.YEAR), testDate.get(Calendar.YEAR)),
                    () -> assertEquals(initialSampleDate.get(Calendar.MONTH), testDate.get(Calendar.MONTH)),
                    () -> assertEquals(initialSampleDate.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(23, testDate.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(59, testDate.get(Calendar.MINUTE)),
                    () -> assertEquals(59, testDate.get(Calendar.SECOND))
            );
        }
    }

    @Nested
    class nextDayStartTest {
        @Test
        @DisplayName("should return the next day with time equal to 00:00:00")
        public void should_return_next_day_start() {

            // given
            testDate.setTime(initialSampleDate.getTime());

            // when
            testDate = TimeUtils.getNextDayStart(testDate.getTime());

            // then
            Calendar nextDay = getNextDay(initialSampleDate);
            assertAll(
                    () -> assertEquals(nextDay.get(Calendar.YEAR), testDate.get(Calendar.YEAR)),
                    () -> assertEquals(nextDay.get(Calendar.MONTH), testDate.get(Calendar.MONTH)),
                    () -> assertEquals(nextDay.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(0, testDate.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(0, testDate.get(Calendar.MINUTE)),
                    () -> assertEquals(0, testDate.get(Calendar.SECOND))
            );
        }

        private Calendar getNextDay(Calendar calendar) {
            Calendar nextDay = Calendar.getInstance();
            nextDay.setTime(initialSampleDate.getTime());
            nextDay.add(Calendar.DAY_OF_MONTH, 1);
            return nextDay;
        }
    }

    @Nested
    class nextMonthStartTest {
        @Test
        @DisplayName("should return the first day of the next month with time equal to 00:00:00")
        public void should_return_next_months_first_day_start() {
            // given
            testDate.setTime(initialSampleDate.getTime());

            // when
            testDate = TimeUtils.getNextMonthStart(testDate.getTime());

            // then
            Calendar firstDayOfNextMonth = getFirstDayOfTheMonth(initialSampleDate);
            assertAll(
                    () -> assertEquals(firstDayOfNextMonth.get(Calendar.YEAR), testDate.get(Calendar.YEAR)),
                    () -> assertEquals(firstDayOfNextMonth.get(Calendar.MONTH), testDate.get(Calendar.MONTH)),
                    () -> assertEquals(firstDayOfNextMonth.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(0, testDate.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(0, testDate.get(Calendar.MINUTE)),
                    () -> assertEquals(0, testDate.get(Calendar.SECOND))
            );
        }

        private Calendar getFirstDayOfTheMonth(Calendar calendar) {
            Calendar firstDayOfTheMonth = Calendar.getInstance();
            firstDayOfTheMonth.setTime(initialSampleDate.getTime());
            firstDayOfTheMonth.add(Calendar.MONTH, 1);
            firstDayOfTheMonth.set(Calendar.DAY_OF_MONTH, 1);
            return firstDayOfTheMonth;
        }
    }

    @Nested
    class monthEndTest {
        @Test
        @DisplayName("should return the last day of the same month with time equal to 23:59:59")
        public void should_return_same_months_last_day_end() {

            // given
            testDate.setTime(initialSampleDate.getTime());

            // when
            testDate = TimeUtils.getMonthEnd(testDate.getTime());

            // then
            Calendar lastDayOfMonth = getLastDayOfMonth(initialSampleDate);
            assertAll(
                    () -> assertEquals(lastDayOfMonth.get(Calendar.YEAR), testDate.get(Calendar.YEAR)),
                    () -> assertEquals(lastDayOfMonth.get(Calendar.MONTH), testDate.get(Calendar.MONTH)),
                    () -> assertEquals(lastDayOfMonth.get(Calendar.DAY_OF_MONTH), testDate.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(23, testDate.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(59, testDate.get(Calendar.MINUTE)),
                    () -> assertEquals(59, testDate.get(Calendar.SECOND))
            );
        }

        private Calendar getLastDayOfMonth(Calendar calendar) {
            Calendar lastDayOfMonth = Calendar.getInstance();
            lastDayOfMonth.setTime(initialSampleDate.getTime());
            lastDayOfMonth.set(Calendar.DAY_OF_MONTH, initialSampleDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            return lastDayOfMonth;
        }
    }

    @Nested
    class nDaysElapsed {

        @Test
        @DisplayName("Should return true only if the actual number of elapsed days is equal or greater from the specified")
        public void should_ReturnTrue_when_atLeast_nDaysElapsed() {
            // given
            Calendar fewDaysLater = Calendar.getInstance();
            fewDaysLater.setTime(initialSampleDate.getTime());

            int numOfDaysElapsed = 5;
            fewDaysLater.add(Calendar.DAY_OF_MONTH, numOfDaysElapsed);

            // when
            boolean hasNDaysElapsed = TimeUtils.hasNDaysElapsed(initialSampleDate, fewDaysLater, numOfDaysElapsed);
            boolean hasLessThanNDaysElapsed = TimeUtils.hasNDaysElapsed(initialSampleDate, fewDaysLater, numOfDaysElapsed - 1);
            boolean hasMoreThanNDaysElapsed = TimeUtils.hasNDaysElapsed(initialSampleDate, fewDaysLater, numOfDaysElapsed + 1);

            // then
            assertAll(
                    () -> assertTrue(hasNDaysElapsed),
                    () -> assertTrue(hasLessThanNDaysElapsed),
                    () -> assertFalse(hasMoreThanNDaysElapsed)
            );

        }

        @Test
        @DisplayName("Should work with precision of one second")
        public void should_work_with_precision_of_one_second() {

            // given
            int numOfDaysElapsed = 5;

            Calendar fewDaysAndSecondLater = Calendar.getInstance();
            fewDaysAndSecondLater.setTime(initialSampleDate.getTime());
            fewDaysAndSecondLater.add(Calendar.DAY_OF_MONTH, numOfDaysElapsed);
            fewDaysAndSecondLater.add(Calendar.SECOND, 1);

            Calendar secondBeforeNDaysElapsed = Calendar.getInstance();
            secondBeforeNDaysElapsed.setTime(initialSampleDate.getTime());
            secondBeforeNDaysElapsed.add(Calendar.DAY_OF_MONTH, numOfDaysElapsed);
            secondBeforeNDaysElapsed.add(Calendar.SECOND, -1);

            // when
            boolean hasNDaysElapsedSecondAfter = TimeUtils.hasNDaysElapsed(initialSampleDate, fewDaysAndSecondLater, numOfDaysElapsed);
            boolean hasNDaysElapsedSecondBefore = TimeUtils.hasNDaysElapsed(initialSampleDate, secondBeforeNDaysElapsed, numOfDaysElapsed);

            // then
            assertAll(
                    () -> assertTrue(hasNDaysElapsedSecondAfter),
                    () -> assertFalse(hasNDaysElapsedSecondBefore)
            );
        }

        @Test
        @DisplayName("Should work on dates with different months")
        public void should_returnTrue_when_datesHaveDifferentMonths() {
            // given
            testDate.setTime(initialSampleDate.getTime());
            int daysElapsed = 45;
            testDate.add(Calendar.DAY_OF_MONTH, daysElapsed);

            // when
            boolean hasNDaysElapsed = TimeUtils.hasNDaysElapsed(initialSampleDate, testDate, daysElapsed);

            // then
            assertTrue(hasNDaysElapsed);
        }

        @Test
        @DisplayName("Should work on dates with different years")
        public void should_ReturnTrue_when_DatesHaveDifferentYears() {
            // given
            testDate.setTime(initialSampleDate.getTime());
            int daysElapsed = 500;
            testDate.add(Calendar.DAY_OF_MONTH, daysElapsed);

            // when
            boolean hasNDaysElapsed = TimeUtils.hasNDaysElapsed(initialSampleDate, testDate, daysElapsed);

            // then
            assertTrue(hasNDaysElapsed);
        }
    }
}
