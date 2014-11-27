package com.webcalendar.util;

import com.webcalendar.exception.DateTimeFormatException;
import org.junit.Test;
import java.time.*;
import static org.junit.Assert.*;

public class DateHelperTest {

    @Test
    public void testStringToDateTime() throws DateTimeFormatException {

        LocalDateTime expectedResult = LocalDateTime.of(2020, Month.OCTOBER, 6, 10, 0);
        LocalDateTime actualResult = DateHelper.stringToDateTime("06-10-2020 10:00");

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = DateTimeFormatException.class)
    public void testStringToDateTimeWrongFormat() throws DateTimeFormatException {

        DateHelper.stringToDateTime("06-10-202 10:00");
    }

    @Test
    public void testStringToDate() throws DateTimeFormatException {

        LocalDate expectedResult = LocalDate.of(2020, Month.OCTOBER, 6);
        LocalDate actualResult = DateHelper.stringToDate("06-10-2020");

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = DateTimeFormatException.class)
    public void testStringToDateWrongFormat() throws DateTimeFormatException {

        DateHelper.stringToDate("06-10-202");

    }

    @Test
    public void testStringToTime() throws DateTimeFormatException {

        LocalTime expectedResult = LocalTime.of(10,0);
        LocalTime actualResult = DateHelper.stringToTime("10:00");

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = DateTimeFormatException.class)
    public void testStringToTimeWrongFormat() throws DateTimeFormatException {

        DateHelper.stringToTime("10:004");
    }


    @Test
    public void testDateTimeToString() throws DateTimeFormatException {

        String expectedResult = "06-10-2020 10:00";
        String actualResult = DateHelper.dateTimeToString(LocalDateTime.of(2020, Month.OCTOBER, 6, 10, 0));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDateToString() throws DateTimeFormatException {

        String expectedResult = "06-10-2020";
        String actualResult = DateHelper.dateToString(LocalDate.of(2020, Month.OCTOBER, 6));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testTimeToString() throws DateTimeFormatException {

        String expectedResult = "10:00";
        String actualResult = DateHelper.timeToString(LocalTime.of(10, 0));

        assertEquals(expectedResult, actualResult);
    }


    @Test
    public void testDateToStringDescription() {

        String expectedResult = "Tue, 6 October 2020";
        String actualResult = DateHelper.dateToStringDescription(LocalDate.of(2020, Month.OCTOBER, 6));

        assertEquals(expectedResult, actualResult);
    }


    @Test
    public void testMonthToStringDescription() {

        String expectedResult = "October 2020";
        LocalDate month = LocalDate.of(2020, Month.OCTOBER, 1);
        String actualResult = DateHelper.monthToStringDescription(month);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void testPeriodToStringDescriptionSameMonths() {

        String expectedResult = "1 - 31, October 2020";

        String actualResult = DateHelper.periodToStringDescription(LocalDate.of(2020, Month.OCTOBER, 1),
                LocalDate.of(2020, Month.OCTOBER, 31));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPeriodToStringDescriptionDifferentMonths() {

        String expectedResult = "1, October - 10, November 2020";

        String actualResult = DateHelper.periodToStringDescription(LocalDate.of(2020, Month.OCTOBER, 1),
                LocalDate.of(2020, Month.NOVEMBER, 10));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPeriodToStringDescriptionDifferentYears() {

        String expectedResult = "28, December 2020 - 3, January 2021";

        String actualResult = DateHelper.periodToStringDescription(LocalDate.of(2020, Month.DECEMBER, 28),
                LocalDate.of(2021, Month.JANUARY, 3));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPeriodWithTimeToStringDescriptionSameMonths() throws DateTimeFormatException {

        String expectedResult = "1, October 10:00 - 31, October 10:00, 2020";

        String actualResult = DateHelper.periodWithTimeToStringDescription(LocalDateTime.of(2020, Month.OCTOBER, 1, 10, 0),
                LocalDateTime.of(2020, Month.OCTOBER, 31, 10, 0));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPeriodWithTimeToStringDescriptionDifferentMonths() throws DateTimeFormatException {

        String expectedResult = "1, October 10:00 - 10, November 10:00, 2020";

        String actualResult = DateHelper.periodWithTimeToStringDescription(LocalDateTime.of(2020, Month.OCTOBER, 1, 10, 0), LocalDateTime.of(2020, Month.NOVEMBER, 10, 10, 0));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testPeriodWithTimeToStringDescriptionDifferentYears() throws DateTimeFormatException {

        String expectedResult = "28, December 10:00, 2020 - 3, January 10:00, 2021";

        String actualResult = DateHelper.periodWithTimeToStringDescription(LocalDateTime.of(2020, Month.DECEMBER, 28, 10, 0),
                LocalDateTime.of(2021, Month.JANUARY, 3, 10, 0));

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testReplaceMonth() {

        String expectedResult1 = "January";
        String expectedResult2 = "February";
        String expectedResult3 = "March";
        String expectedResult4 = "April";
        String expectedResult5 = "May";
        String expectedResult6 = "June";
        String expectedResult7 = "July";
        String expectedResult8 = "August";
        String expectedResult9 = "September";
        String expectedResult10 = "October";
        String expectedResult11 = "November";
        String expectedResult12 = "December";

        String actualResult1 = DateHelper.replaceMonth(Month.JANUARY);
        String actualResult2 = DateHelper.replaceMonth(Month.FEBRUARY);
        String actualResult3 = DateHelper.replaceMonth(Month.MARCH);
        String actualResult4 = DateHelper.replaceMonth(Month.APRIL);
        String actualResult5 = DateHelper.replaceMonth(Month.MAY);
        String actualResult6 = DateHelper.replaceMonth(Month.JUNE);
        String actualResult7 = DateHelper.replaceMonth(Month.JULY);
        String actualResult8 = DateHelper.replaceMonth(Month.AUGUST);
        String actualResult9 = DateHelper.replaceMonth(Month.SEPTEMBER);
        String actualResult10 = DateHelper.replaceMonth(Month.OCTOBER);
        String actualResult11 = DateHelper.replaceMonth(Month.NOVEMBER);
        String actualResult12 = DateHelper.replaceMonth(Month.DECEMBER);

        assertEquals(expectedResult1, actualResult1);
        assertEquals(expectedResult2, actualResult2);
        assertEquals(expectedResult3, actualResult3);
        assertEquals(expectedResult4, actualResult4);
        assertEquals(expectedResult5, actualResult5);
        assertEquals(expectedResult6, actualResult6);
        assertEquals(expectedResult7, actualResult7);
        assertEquals(expectedResult8, actualResult8);
        assertEquals(expectedResult9, actualResult9);
        assertEquals(expectedResult10, actualResult10);
        assertEquals(expectedResult11, actualResult11);
        assertEquals(expectedResult12, actualResult12);

    }

    @Test
    public void testReplaceDay() {

        String expectedResult1 = "Mon";
        String expectedResult2 = "Tue";
        String expectedResult3 = "Wed";
        String expectedResult4 = "Thu";
        String expectedResult5 = "Fri";
        String expectedResult6 = "Sat";
        String expectedResult7 = "Sun";

        String actualResult1 = DateHelper.replaceDay(DayOfWeek.MONDAY);
        String actualResult2 = DateHelper.replaceDay(DayOfWeek.TUESDAY);
        String actualResult3 = DateHelper.replaceDay(DayOfWeek.WEDNESDAY);
        String actualResult4 = DateHelper.replaceDay(DayOfWeek.THURSDAY);
        String actualResult5 = DateHelper.replaceDay(DayOfWeek.FRIDAY);
        String actualResult6 = DateHelper.replaceDay(DayOfWeek.SATURDAY);
        String actualResult7 = DateHelper.replaceDay(DayOfWeek.SUNDAY);

        assertEquals(expectedResult1, actualResult1);
        assertEquals(expectedResult2, actualResult2);
        assertEquals(expectedResult3, actualResult3);
        assertEquals(expectedResult4, actualResult4);
        assertEquals(expectedResult5, actualResult5);
        assertEquals(expectedResult6, actualResult6);
        assertEquals(expectedResult7, actualResult7);
    }
}
