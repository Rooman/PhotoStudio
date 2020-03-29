package com.photostudio.dao.jdbc.entity;

import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.entity.EmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailTemplateRow {
    int langId;
    OrderStatus orderStatus;
    EmailTemplate emailTemplate;
}
