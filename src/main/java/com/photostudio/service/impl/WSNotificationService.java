package com.photostudio.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.entity.user.User;
import com.photostudio.service.WebNotificationService;
import com.photostudio.service.entity.OrderIdAndStatusDto;
import com.photostudio.service.entity.UserWSSession;
import lombok.SneakyThrows;

import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WSNotificationService implements WebNotificationService {
    private ObjectMapper objectMapper;

    private List<UserWSSession> sessionList = new CopyOnWriteArrayList<>();

    public WSNotificationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public void notification(int orderId, User user, String message) {
        for (UserWSSession wsSession : sessionList) {
            User userSession = wsSession.getUser();
            if (userSession.getId() == user.getId()) {
                String sendMessage = createMessage(orderId, message);
                Session session = wsSession.getSession();
                session.getBasicRemote().sendText(sendMessage);
            }
        }
    }

    @Override
    public void addSession(UserWSSession session) {
        sessionList.add(session);
    }

    @Override
    public void removeSession(Session session) {
        for (UserWSSession wsSession : sessionList) {
            if (wsSession.getSession().equals(session)) {
                sessionList.remove(wsSession);
            }
        }
    }

    @SneakyThrows
    String createMessage(int orderId, String message) {
        OrderIdAndStatusDto orderIdAndStatusDto = new OrderIdAndStatusDto(orderId, message);
        return objectMapper.writeValueAsString(orderIdAndStatusDto);
    }
}
