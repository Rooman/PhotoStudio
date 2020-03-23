package com.photostudio.service.impl;


import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.MailService;
import com.photostudio.service.UserService;
import com.photostudio.service.entity.EmailTemplate;
import com.photostudio.web.util.MailSender;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DefaultMailService implements MailService {

    private MailSender mailSender;
    Map<OrderStatus, EmailTemplate> mapOfTemplates;

    public DefaultMailService(MailSender mailSender) {
        mapOfTemplates = new HashMap<>();
        mapOfTemplates.put(OrderStatus.VIEW_AND_SELECT, new EmailTemplate("Order %d is created", "You can choose photo in order %d"));
        mapOfTemplates.put(OrderStatus.SELECTED, new EmailTemplate("User %s selected photo for order:%d", "User %s selected photo for order:%d"));
        mapOfTemplates.put(OrderStatus.READY, new EmailTemplate("Order %d is ready", "You can download selected photos in the order %d"));

        this.mailSender = mailSender;
    }


    @Override
    public void sendOnChangeStatus(User user, int orderId, OrderStatus orderStatus) {
        log.info("Send mail to {} after changing status to {} in order {}", user.getEmail(), orderStatus, orderId);
        String userMail = user.getEmail();
        if (orderStatus == OrderStatus.SELECTED) {
            mailSender.sendToAdmin(mapOfTemplates.get(OrderStatus.SELECTED).generateHeader(userMail, orderId), mapOfTemplates.get(OrderStatus.SELECTED).generateBody(userMail, orderId));
        } else {
            mailSender.send(mapOfTemplates.get(orderStatus).generateHeader(orderId), mapOfTemplates.get(orderStatus).generateBody(orderId), userMail);
        }
    }
}
