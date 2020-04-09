package com.photostudio.web.ws;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.security.SecurityService;
import com.photostudio.service.WebNotificationService;
import com.photostudio.service.entity.UserWSSession;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/notification", configurator = MyEndpointConfigurator.class)
@Slf4j
public class NotificationEndpoint {
    private WebNotificationService webNotificationService = ServiceLocator.getService(WebNotificationService.class);
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    @OnOpen
    public void onOpen(Session session) {
        String userToken = (String) session.getUserProperties().get("user-token");
        User user = securityService.getSession(userToken).getUser();
        session.setMaxIdleTimeout(2*60*60*1000);
        UserWSSession userWSSession = new UserWSSession(session, user);
        webNotificationService.addSession(userWSSession);
    }



    @OnClose
    public void onClose(Session session) {
        webNotificationService.removeSession(session);
    }

    @OnError
    public void onError(Throwable throwable) {
        log.error("Error in webSocket connection", throwable);
    }

}
