package com.photostudio.dao;


import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.entity.EmailTemplate;

public interface EmailTemplateDao {
    EmailTemplate getByLangAndStatus(int langId, OrderStatus orderStatus);
}
