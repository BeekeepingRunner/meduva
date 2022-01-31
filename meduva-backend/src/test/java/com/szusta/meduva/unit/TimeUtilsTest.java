package com.szusta.meduva.unit;

import com.szusta.meduva.util.TimeUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class TimeUtilsTest {

    static DateFormat dateFormat;

    @BeforeAll
    static void setUp() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Nested
    class getDayStartTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/test-dates/test_dates.csv")
        @DisplayName("should return the same day as the input, with time equal to 00:00:00")
        public void should_returnSameDayWithCorrectTime(String dateStr) throws ParseException {
            // given
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(dateFormat.parse(dateStr));

            // when
            Calendar actualDate = TimeUtils.getDayStart(initialDate.getTime());

            // then
            assertAll(
                    () -> assertEquals(initialDate.get(Calendar.YEAR), actualDate.get(Calendar.YEAR)),
                    () -> assertEquals(initialDate.get(Calendar.MONTH), actualDate.get(Calendar.MONTH)),
                    () -> assertEquals(initialDate.get(Calendar.DAY_OF_MONTH), actualDate.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(0, actualDate.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(0, actualDate.get(Calendar.MINUTE)),
                    () -> assertEquals(0, actualDate.get(Calendar.SECOND))
            );
        }
    }


    @Nested
    class getDayEndTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/test-dates/test_dates.csv")
        @DisplayName("should return same day with time equal to 23:59:59")
        public void should_return_same_day_end(String dateStr) throws ParseException {
            // given
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(dateFormat.parse(dateStr));

            // when
            Calendar actual = TimeUtils.getDayEnd(initialDate.getTime());

            // then
            assertAll(
                    () -> assertEquals(initialDate.get(Calendar.YEAR), actual.get(Calendar.YEAR)),
                    () -> assertEquals(initialDate.get(Calendar.MONTH), actual.get(Calendar.MONTH)),
                    () -> assertEquals(initialDate.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(23, actual.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(59, actual.get(Calendar.MINUTE)),
                    () -> assertEquals(59, actual.get(Calendar.SECOND))
            );
        }
    }

    @Nested
    class getNextDayStartTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/test-dates/test_dates.csv")
        @DisplayName("should return the next day with time equal to 00:00:00")
        public void should_return_next_day_start(String dateStr) throws ParseException {

            // given
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(dateFormat.parse(dateStr));

            // when
            Calendar actual = TimeUtils.getNextDayStart(initialDate.getTime());

            // then
            Calendar expectedTime = getNextDay(initialDate);
            assertAll(
                    () -> assertEquals(expectedTime.get(Calendar.YEAR), actual.get(Calendar.YEAR)),
                    () -> assertEquals(expectedTime.get(Calendar.MONTH), actual.get(Calendar.MONTH)),
                    () -> assertEquals(expectedTime.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(0, actual.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(0, actual.get(Calendar.MINUTE)),
                    () -> assertEquals(0, actual.get(Calendar.SECOND))
            );
        }

        private Calendar getNextDay(Calendar baseDate) {
            Calendar nextDay = Calendar.getInstance();
            nextDay.setTime(baseDate.getTime());
            nextDay.add(Calendar.DAY_OF_MONTH, 1);
            return nextDay;
        }
    }

    @Nested
    class getNextMonthStartTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/test-dates/test_dates.csv")
        @DisplayName("should return the first day of the next month with time equal to 00:00:00")
        public void should_return_next_months_first_day_start(String dateStr) throws ParseException {

            // given
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(dateFormat.parse(dateStr));

            // when
            Calendar actual = TimeUtils.getNextMonthStart(initialDate.getTime());

            // then
            Calendar expectedTime = getFirstDayOfTheMonth(initialDate);
            assertAll(
                    () -> assertEquals(expectedTime.get(Calendar.YEAR), actual.get(Calendar.YEAR)),
                    () -> assertEquals(expectedTime.get(Calendar.MONTH), actual.get(Calendar.MONTH)),
                    () -> assertEquals(expectedTime.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(0, actual.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(0, actual.get(Calendar.MINUTE)),
                    () -> assertEquals(0, actual.get(Calendar.SECOND))
            );
        }

        private Calendar getFirstDayOfTheMonth(Calendar baseDate) {
            Calendar firstDayOfTheMonth = Calendar.getInstance();
            firstDayOfTheMonth.setTime(baseDate.getTime());
            firstDayOfTheMonth.add(Calendar.MONTH, 1);
            firstDayOfTheMonth.set(Calendar.DAY_OF_MONTH, 1);
            return firstDayOfTheMonth;
        }
    }

    @Nested
    class getMonthEndTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/test-dates/test_dates.csv")
        @DisplayName("should return the last day of the same month with time equal to 23:59:59")
        public void should_return_same_months_last_day_end(String dateStr) throws ParseException {

            // given
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(dateFormat.parse(dateStr));

            // when
            Calendar actual = TimeUtils.getMonthEnd(initialDate.getTime());

            // then
            Calendar expectedTime = getLastDayOfMonth(initialDate);
            assertAll(
                    () -> assertEquals(expectedTime.get(Calendar.YEAR), actual.get(Calendar.YEAR)),
                    () -> assertEquals(expectedTime.get(Calendar.MONTH), actual.get(Calendar.MONTH)),
                    () -> assertEquals(expectedTime.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH)),
                    () -> assertEquals(23, actual.get(Calendar.HOUR_OF_DAY)),
                    () -> assertEquals(59, actual.get(Calendar.MINUTE)),
                    () -> assertEquals(59, actual.get(Calendar.SECOND))
            );
        }

        private Calendar getLastDayOfMonth(Calendar baseDate) {
            Calendar lastDayOfMonth = Calendar.getInstance();
            lastDayOfMonth.setTime(baseDate.getTime());
            lastDayOfMonth.set(Calendar.DAY_OF_MONTH, baseDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            return lastDayOfMonth;
        }
    }

    @Nested
    class hasNDaysElapsedTests {

        @ParameterizedTest
        @CsvFileSource(resources = "/test-dates/test_dates_with_days_elapsed.csv")
        @DisplayName("Should return true only if the actual number of elapsed days is equal or greater from the specified")
        public void should_ReturnTrue_when_atLeast_nDaysElapsed(String dateStr, String nDays) throws ParseException {
            // given
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(dateFormat.parse(dateStr));

            int numOfDaysElapsed = Integer.parseInt(nDays);
            Calendar fewDaysLater = Calendar.getInstance();
            fewDaysLater.setTime(initialDate.getTime());
            fewDaysLater.add(Calendar.DAY_OF_MONTH, numOfDaysElapsed);

            // when
            boolean hasNDaysElapsed = TimeUtils.hasNDaysElapsed(initialDate, fewDaysLater, numOfDaysElapsed);
            boolean hasLessThanNDaysElapsed = TimeUtils.hasNDaysElapsed(initialDate, fewDaysLater, numOfDaysElapsed - 1);
            boolean hasMoreThanNDaysElapsed = TimeUtils.hasNDaysElapsed(initialDate, fewDaysLater, numOfDaysElapsed + 1);

            // then
            assertAll(
                    () -> assertTrue(hasNDaysElapsed),
                    () -> assertTrue(hasLessThanNDaysElapsed),
                    () -> assertFalse(hasMoreThanNDaysElapsed)
            );

        }

        @ParameterizedTest
        @CsvFileSource(resources = "/test-dates/test_dates_with_days_elapsed.csv")
        @DisplayName("Should work with precision of one millisecond")
        public void should_work_with_precision_of_one_millisecond(String dateStr, String nDays) throws ParseException {

            // given
            Calendar initialDate = Calendar.getInstance();
            initialDate.setTime(dateFormat.parse(dateStr));

            int numOfDaysElapsed = Integer.parseInt(nDays);

            Calendar fewDaysLaterPlusOneMs = Calendar.getInstance();
            fewDaysLaterPlusOneMs.setTime(initialDate.getTime());
            fewDaysLaterPlusOneMs.add(Calendar.DAY_OF_MONTH, numOfDaysElapsed);
            fewDaysLaterPlusOneMs.add(Calendar.MILLISECOND, 1);

            Calendar fewDaysLaterMinusOneMs = Calendar.getInstance();
            fewDaysLaterMinusOneMs.setTime(initialDate.getTime());
            fewDaysLaterMinusOneMs.add(Calendar.DAY_OF_MONTH, numOfDaysElapsed);
            fewDaysLaterMinusOneMs.add(Calendar.MILLISECOND, -1);

            // when
            boolean hasNDaysElapsedMillisecondAfter = TimeUtils
                    .hasNDaysElapsed(initialDate, fewDaysLaterPlusOneMs, numOfDaysElapsed);

            boolean hasNDaysElapsedMillisecondBefore = TimeUtils
                    .hasNDaysElapsed(initialDate, fewDaysLaterMinusOneMs, numOfDaysElapsed);

            // then
            assertAll(
                    () -> assertTrue(hasNDaysElapsedMillisecondAfter),
                    () -> assertFalse(hasNDaysElapsedMillisecondBefore)
            );
        }
    }
}
