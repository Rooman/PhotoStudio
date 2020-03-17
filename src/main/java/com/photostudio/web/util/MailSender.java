package com.photostudio.web.util;

import com.photostudio.util.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private String adminEmail;
    private String password;
    private Properties properties;

    public MailSender() {
        properties = new PropertyReader("mail.properties").getProperties();
        this.adminEmail = properties.getProperty("mail.admin.email");
        this.password = properties.getProperty("mail.admin.password");
    }

    public void send(String subject, String text, String toEmail) {
        LOG.info("Try to send mail to {}", toEmail);
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error("Mail was not send to {}", toEmail, e);
            throw new RuntimeException("Mail was not send to " + toEmail, e);
        }
    }

    public void sendToAdmin(String subject, String text) {
        send(subject, text, adminEmail);
    }
}
