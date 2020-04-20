package com.photostudio.web.util;

import com.photostudio.util.PropertyReader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@NoArgsConstructor
public class MailSender {
    private String adminEmail;
    private String password;
    private PropertyReader propertyReader;

    public MailSender(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
        this.adminEmail = propertyReader.getString("mail.admin.email");
        this.password = propertyReader.getString("mail.admin.password");
    }

    public void send(String subject, String text, String toEmail) {
        log.info("Try to send mail to {}", toEmail);
        Session session = Session.getInstance(propertyReader.getProperties(), new Authenticator() {
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
            log.error("Mail was not send to {}", toEmail, e);
            throw new RuntimeException("Mail was not send to " + toEmail, e);
        }
    }

    public void sendToAdmin(String subject, String text) {
        send(subject, text, adminEmail);
    }
}

// данные локига интерфейс
// dao   service <- web
// dao service <- desktop