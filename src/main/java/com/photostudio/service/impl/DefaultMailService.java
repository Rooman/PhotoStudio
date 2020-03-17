package com.photostudio.service.impl;


import com.photostudio.entity.user.User;
import com.photostudio.service.MailService;
import com.photostudio.service.UserService;
import com.photostudio.web.util.MailSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultMailService implements MailService {

    private MailSender mailSender;

    public DefaultMailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendOnChangeStatus(User user, long orderId, int orderStatusId) {
        log.info("Send mail to {} after changing status to {} in order {}", user.getEmail(), orderStatusId, orderId);
        String userMail = user.getEmail();
        switch (orderStatusId) {
            case 2: {
                mailSender.send("Order " + orderId + " is created", "You can choose photo in order " + orderId, userMail);
                break;
            }
            case 3: {
                mailSender.sendToAdmin("User " + userMail + " selected photo for order:" + orderId, "User " + userMail + " selected photo for order:" + orderId);
                break;
            }
            case 4: {
                mailSender.send("Order " + orderId + " is ready", "You can download selected photos", userMail);
                break;
            }
        }
    }
}
