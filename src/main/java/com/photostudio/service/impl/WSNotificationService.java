package com.photostudio.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.entity.user.User;
import com.photostudio.service.WebNotificationService;
import com.photostudio.service.entity.OrderIdAndMessageText;
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
    public void notification(User user, OrderIdAndMessageText orderIdAndMessageText) {
        String sendMessageJSON = createMessage(orderIdAndMessageText);
        for (UserWSSession wsSession : sessionList) {
            User userSession = wsSession.getUser();
            if (userSession.getId() == user.getId()) {
                Session session = wsSession.getSession();
                session.getBasicRemote().sendText(sendMessageJSON);
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
    String createMessage(OrderIdAndMessageText orderIdAndMessageText) {
        return objectMapper.writeValueAsString(orderIdAndMessageText);
    }
}
