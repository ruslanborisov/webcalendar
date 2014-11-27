package com.webcalendar.service;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    private static final Logger logger = Logger.getLogger(EmailServiceImpl.class);

    private final String USERNAME = "calendarwebapp@gmail.com";
    private final String PASSWORD = "calendarwebapp777";

    @Override
    public void sendEmail(String subject, String text, String emailTo) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);
            message.setText(text);

            logger.info("Sending email to '" + emailTo + "'");
            Transport.send(message);
            logger.info("Email sent successfully");

        } catch (MessagingException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
