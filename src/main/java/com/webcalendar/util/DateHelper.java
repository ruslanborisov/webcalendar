package com.webcalendar.util;

import com.webcalendar.exception.DateTimeFormatException;
import org.apache.log4j.Logger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Provides ability to parse/format
 * string to {@link LocalDateTime} / {@link LocalDateTime} to string,
 * string to {@link LocalDate} / {@link LocalDate} to string,
 * and other operation with date/datetime conversion
 *
 * Using format of string: "15-10-2020 10:00"
 *
 *
 * @author Ruslan Borisov
 */
public class DateHelper {

    private static final Logger logger = Logger.getLogger(DateHelper.class);
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm";
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    private static final String TIME_PATTERN = "HH:mm";

    public static LocalDateTime stringToDateTime(String stringDateTime) throws DateTimeFormatException {
        if (stringDateTime==null)
            throw new IllegalArgumentException();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        try {
            return LocalDateTime.parse(stringDateTime, formatter);
        } catch (DateTimeParseException dtpe) {
            logger.error(dtpe.getMessage());
            throw new DateTimeFormatException("Wrong format of date/time");
        }
    }

    public static LocalDate stringToDate(String stringDate) throws DateTimeFormatException {
        if (stringDate==null)
            throw new IllegalArgumentException();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            return LocalDate.parse(stringDate, formatter);
        } catch (DateTimeParseException dtpe) {
            logger.error(dtpe.getMessage());
            throw new DateTimeFormatException("Wrong format of date");
        }

    }

    public static LocalTime stringToTime(String stringTime) throws DateTimeFormatException {
        if (stringTime==null)
            throw new IllegalArgumentException();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        try {
            return LocalTime.parse(stringTime, formatter);
        } catch (DateTimeParseException dtpe) {
            logger.error(dtpe.getMessage());
            throw new DateTimeFormatException("Wrong format of time");
        }

    }

    public static String dateTimeToString(LocalDateTime dateTime) throws DateTimeFormatException {
        if (dateTime==null)
            throw new IllegalArgumentException();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

        try {
            return dateTime.format(formatter);
        } catch (DateTimeException dte) {
            logger.error(dte.getMessage());
            throw new DateTimeFormatException("Wrong value of year ,month, day, hour or minute");
        }
    }

    public static String dateToString(LocalDate date) throws DateTimeFormatException {
        if (date==null)
            throw new IllegalArgumentException();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        try {
            return date.format(formatter);
        } catch (DateTimeException dte) {
            logger.error(dte.getMessage());
            throw new DateTimeFormatException("Wrong value of year ,month or day");
        }
    }

    public static String timeToString(LocalTime time) throws DateTimeFormatException {
        if (time==null)
            throw new IllegalArgumentException();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

        try {
            return time.format(formatter);
        } catch (DateTimeException dte) {
            logger.error(dte.getMessage());
            throw new DateTimeFormatException("Wrong value of hour or minute");
        }
    }

    public static String dateToStringDescription(LocalDate date) {
        StringBuilder sb = new StringBuilder();
        sb.append(replaceDay(date.getDayOfWeek()))
                .append(", ")
                .append(date.getDayOfMonth())
                .append(" ")
                .append(replaceMonth(date.getMonth()))
                .append(" ")
                .append(date.getYear());

        return sb.toString();
    }

    public static String monthToStringDescription(LocalDate date) {

        StringBuilder sb = new StringBuilder();
        sb.append(replaceMonth(date.getMonth()))
          .append(" ")
          .append(date.getYear());

        return sb.toString();
    }

    public static String periodToStringDescription(LocalDate startDate, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();


        if (startDate.equals(endDate)) {
            sb.append(startDate.getDayOfMonth())
                    .append(", ")
                    .append(replaceMonth(startDate.getMonth()))
                    .append(" ")
                    .append(startDate.getYear());

        } else {
            if (startDate.getMonth().equals(endDate.getMonth())) {
                sb.append(startDate.getDayOfMonth())
                        .append(" - ")
                        .append(endDate.getDayOfMonth())
                        .append(", ")
                        .append(replaceMonth(startDate.getMonth()))
                        .append(" ")
                        .append(startDate.getYear());
            } else {
                if (startDate.getYear() == (endDate.getYear())) {
                    sb.append(startDate.getDayOfMonth())
                            .append(", ")
                            .append(replaceMonth(startDate.getMonth()))
                            .append(" - ")
                            .append(endDate.getDayOfMonth())
                            .append(", ")
                            .append(replaceMonth(endDate.getMonth()))
                            .append(" ")
                            .append(startDate.getYear());
                } else {
                    sb.append(startDate.getDayOfMonth())
                            .append(", ")
                            .append(replaceMonth(startDate.getMonth()))
                            .append(" ")
                            .append(startDate.getYear())
                            .append(" - ")
                            .append(endDate.getDayOfMonth())
                            .append(", ")
                            .append(replaceMonth(endDate.getMonth()))
                            .append(" ")
                            .append(endDate.getYear());
                }
            }
        }
        return sb.toString();
    }

    public static String periodWithTimeToStringDescription(LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws DateTimeFormatException {
        StringBuilder sb = new StringBuilder();

        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {

            sb.append(startDateTime.getDayOfMonth())
                    .append(", ")
                    .append(replaceMonth(startDateTime.getMonth()))
                    .append(" ")
                    .append(startDateTime.getYear())
                    .append(", ")
                    .append(timeToString(startDateTime.toLocalTime()))
                    .append(" - ")
                    .append(timeToString(endDateTime.toLocalTime()));

        } else {
            if (startDateTime.getYear() == (endDateTime.getYear())) {
                sb.append(startDateTime.getDayOfMonth())
                        .append(", ")
                        .append(replaceMonth(startDateTime.getMonth()))
                        .append(" ")
                        .append(timeToString(startDateTime.toLocalTime()))
                        .append(" - ")
                        .append(endDateTime.getDayOfMonth())
                        .append(", ")
                        .append(replaceMonth(endDateTime.getMonth()))
                        .append(" ")
                        .append(timeToString(endDateTime.toLocalTime()))
                        .append(", ")
                        .append(startDateTime.getYear());

            } else {
                sb.append(startDateTime.getDayOfMonth())
                        .append(", ")
                        .append(replaceMonth(startDateTime.getMonth()))
                        .append(" ")
                        .append(timeToString(startDateTime.toLocalTime()))
                        .append(", ")
                        .append(startDateTime.getYear())
                        .append(" - ")
                        .append(endDateTime.getDayOfMonth())
                        .append(", ")
                        .append(replaceMonth(endDateTime.getMonth()))
                        .append(" ")
                        .append(timeToString(endDateTime.toLocalTime()))
                        .append(", ")
                        .append(endDateTime.getYear());
            }
        }
        return sb.toString();
    }

    public static String replaceMonth(Month month) {
        switch(month) {
            case JANUARY:
               return "January";
            case FEBRUARY:
                return "February";
            case MARCH:
                return "March";
            case APRIL:
                return "April";
            case MAY:
                return "May";
            case JUNE:
                return "June";
            case JULY:
                return "July";
            case AUGUST:
                return "August";
            case SEPTEMBER:
                return "September";
            case OCTOBER:
                return "October";
            case NOVEMBER:
                return "November";
            case DECEMBER:
                return "December";
        }
        return "";
    }

    public static String replaceDay(DayOfWeek day) {
        switch(day) {
            case MONDAY:
                return "Mon";
            case TUESDAY:
                return "Tue";
            case WEDNESDAY:
                return "Wed";
            case THURSDAY:
                return "Thu";
            case FRIDAY:
                return "Fri";
            case SATURDAY:
                return "Sat";
            case SUNDAY:
                return "Sun";
        }
        return "";
    }
}
