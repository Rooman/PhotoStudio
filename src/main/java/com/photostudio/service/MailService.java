package com.photostudio.service;


import com.photostudio.entity.user.User;

public interface MailService {

    void sendOnChangeStatus(User user, long orderId, int orderStatusId);
}
