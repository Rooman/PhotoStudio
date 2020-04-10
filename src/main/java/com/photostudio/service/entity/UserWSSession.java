package com.photostudio.service.entity;

import com.photostudio.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.websocket.Session;

@Data
@AllArgsConstructor
public class UserWSSession {
    private Session session;
    private User user;
}
