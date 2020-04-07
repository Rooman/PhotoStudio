package com.photostudio.dao;


import com.photostudio.entity.email.MessageType;
import com.photostudio.entity.email.PasswordEmailTemplate;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.entity.EmailTemplate;

public interface EmailTemplateDao {
    EmailTemplate getByLangAndStatus(int langId, OrderStatus orderStatus);

    PasswordEmailTemplate getPasswordEmailTemplateByLangIdAndMessageType(int langId, MessageType messageType);
}
