package com.webcalendar.exception;


/**
 * Throws if an error occurred during parsing or format string/date
 */
public class DateTimeFormatException extends Exception {

    public DateTimeFormatException(String msg) {
        super(msg);
    }
    public DateTimeFormatException() {
    }
}
