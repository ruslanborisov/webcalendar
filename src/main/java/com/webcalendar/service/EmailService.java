package com.webcalendar.service;

/**
 * Provides ability to send email.
 *
 *
 * @author Ruslan Borisov
 */
public interface EmailService {

    /**
     * Provides ability to send email with TLS authentication
     *
     * @param subject: Subject of the sending email
     * @param text: Text of the sending email
     * @param emailTo: Email address to send
     */
    void sendEmail(String subject, String text, String emailTo);
}
