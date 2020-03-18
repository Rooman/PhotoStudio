package com.photostudio.service.testUtils;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.web.util.MailSender;


public class MockMailSender extends MailSender {

    private TestDataSource dataSource;

    public MockMailSender(TestDataSource dataSource) {
        this.dataSource=dataSource;
        dataSource.execUpdate("CREATE TABLE IF NOT EXISTS TestSentMails (mailto VARCHAR(50), subject VARCHAR(2000), text VARCHAR(4000));");
    }

    @Override
    public void send(String subject, String text, String toEmail) {
        dataSource.execUpdate("INSERT INTO TestSentMails(mailto, subject, text) VALUES ('" + toEmail+"', '" + subject +"', '" + text +"');");
        System.out.println(subject);
    }

    @Override
    public void sendToAdmin(String subject, String text) {
        send(subject, text, "admin@test.com");
    }
}
