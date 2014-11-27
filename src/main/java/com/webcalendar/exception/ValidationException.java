package com.webcalendar.exception;

/**
 * Throws if happen exception for validate event.
 */
public class ValidationException extends Exception {

    public ValidationException(String msg) {
        super(msg);
    }
    public ValidationException() {
    }
}
