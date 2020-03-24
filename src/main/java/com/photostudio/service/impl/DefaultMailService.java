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

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
public class DefaultMailService implements MailService {

    private MailSender mailSender;
    private UserService userService;
    private Map<OrderStatus, EmailTemplate> mapOfTemplates;

    public DefaultMailService(MailSender mailSender, UserService userService) {
        mapOfTemplates = new HashMap<>();
        mapOfTemplates.put(OrderStatus.VIEW_AND_SELECT, new EmailTemplate("Order %d is created", "You can choose photo in order %d"));
        mapOfTemplates.put(OrderStatus.SELECTED, new EmailTemplate("User %s selected photo for order: %d", "User %s selected photo for order: %d"));
        mapOfTemplates.put(OrderStatus.READY, new EmailTemplate("Order %d is ready", "You can download selected photos in the order %d"));

        this.mailSender = mailSender;
        this.userService = userService;
    }

    @Override
    public void sendOnChangeStatus(User userChanged, int orderId, OrderStatus orderStatus) {
        try {
            if (orderStatus == OrderStatus.SELECTED) {
                sendOnChangeStatusToAdmin(userChanged, orderId, orderStatus);
            } else {
                User userOrdered = userService.getUserByOrderId(orderId);
                sendOnChangeStatusToUser(userOrdered, orderId, orderStatus);
            }
        } catch (Exception e) {
            log.error("Error during sending email", e);
            throw new RuntimeException(e);
        }
    }


    private void sendOnChangeStatusToAdmin(User userChanged, int orderId, OrderStatus orderStatus) {
        String userMail = userChanged.getEmail();
        log.info("Send mail to admin after changing status to {} in order {} by user {}", orderStatus, orderId, userMail);
        mailSender.sendToAdmin(mapOfTemplates.get(orderStatus).generateHeader(userMail, orderId), mapOfTemplates.get(orderStatus).generateBody(userMail, orderId));
    }

    private void sendOnChangeStatusToUser(User userOrdered, int orderId, OrderStatus orderStatus) {
        String userMail = userOrdered.getEmail();
        log.info("Send mail to {} after changing status to {} in order {}", userMail, orderStatus, orderId);
        mailSender.send(mapOfTemplates.get(orderStatus).generateHeader(orderId), mapOfTemplates.get(orderStatus).generateBody(orderId), userMail);
    }
}
