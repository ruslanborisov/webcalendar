package com.webcalendar.exception;

/**
 * Throws if start date is after end date
 */
public class OrderOfArgumentsException extends Exception {

    public OrderOfArgumentsException(String msg) {
        super(msg);
    }
    public OrderOfArgumentsException() {
    }
}
