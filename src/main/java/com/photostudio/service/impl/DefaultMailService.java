package com.photostudio.service.impl;


import com.photostudio.dao.EmailTemplateDao;
import com.photostudio.entity.email.MessageType;
import com.photostudio.entity.email.PasswordEmailTemplate;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.MailService;
import com.photostudio.service.NotificationService;
import com.photostudio.service.UserService;
import com.photostudio.service.entity.EmailTemplate;
import com.photostudio.web.util.MailSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultMailService implements MailService {

    private MailSender mailSender;
    private UserService userService;
    private EmailTemplateDao emailTemplateDao;
    private NotificationService notificationService;

    public DefaultMailService(MailSender mailSender, UserService userService, EmailTemplateDao emailTemplateDao) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.emailTemplateDao = emailTemplateDao;
    }

    public DefaultMailService(MailSender mailSender, UserService userService, EmailTemplateDao emailTemplateDao, NotificationService notificationService) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.emailTemplateDao = emailTemplateDao;
        this.notificationService = notificationService;
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

    @Override
    public void sendNewPassword(User user, String password) {
        log.info("Send mail to user {} with new password", user.getEmail());
        PasswordEmailTemplate passwordEmailTemplate = emailTemplateDao
                .getPasswordEmailTemplateByLangIdAndMessageType(user.getLangId(), MessageType.RESET_PASSWORD);
        mailSender.send(passwordEmailTemplate.getSubject(), passwordEmailTemplate.getBody()
                .replace("<password>", password), user.getEmail());
    }


    private void sendOnChangeStatusToAdmin(User userChanged, int orderId, OrderStatus orderStatus) {
        String userMail = userChanged.getEmail();
        log.info("Send mail to admin after changing status to {} in order {} by user {}", orderStatus, orderId, userMail);
        EmailTemplate emailTemplate = emailTemplateDao.getByLangAndStatus(userChanged.getLangId(), orderStatus);
        mailSender.sendToAdmin(emailTemplate.generateHeader(userMail, orderId), emailTemplate.generateBody(userMail, orderId));
        notificationService.notification(orderId, userChanged, emailTemplate.generateHeader(userMail, orderId));
    }

    private void sendOnChangeStatusToUser(User userOrdered, int orderId, OrderStatus orderStatus) {
        String userMail = userOrdered.getEmail();
        log.info("Send mail to {} after changing status to {} in order {}", userMail, orderStatus, orderId);
        EmailTemplate emailTemplate = emailTemplateDao.getByLangAndStatus(userOrdered.getLangId(), orderStatus);
        mailSender.send(emailTemplate.generateHeader(orderId), emailTemplate.generateBody(orderId), userMail);
        notificationService.notification(orderId, userOrdered, emailTemplate.generateHeader(orderId) + " " + emailTemplate.generateBody(orderId));
    }
}
