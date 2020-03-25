package com.photostudio.security.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.exception.LoginPasswordInvalidException;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class DefaultSecurityService implements SecurityService {
    private UserService userService = ServiceLocator.getService(UserService.class);
    private List<Session> sessionList = new CopyOnWriteArrayList<>();

    @Override
    public Session login(String login, String password) {
        log.info("Connect user by login: {}", login);
        User user = userService.getUserByLogin(login);
        if (user != null) {
            String hashedPassword = getHashedPassword(user.getSalt(), password);
            String actualPassword = user.getPasswordHash();
            if (!hashedPassword.equals(actualPassword)) {
                log.error("Login/password invalid for user with login: {}", login);
                throw new LoginPasswordInvalidException("Login/password invalid for user with login: " + login);
            }
            String userToken = UUID.randomUUID().toString();
            Session session = Session.builder().user(user)
                    .token(userToken).expireDate(LocalDateTime.now().plusHours(2)).build();
            sessionList.add(session);
            log.info("User with login: {} is logged in", login);
            return session;
        } else {
            log.error("User with login: {} not found", login);
            throw new LoginPasswordInvalidException("Login/password invalid for user" + login);
        }
    }

    @Override
    public void logout(Session session) {
        sessionList.remove(session);
    }

    @Override
    public Session getSession(String userToken) {
        if(userToken!=null) {
            Iterator<Session> sessionIterator = sessionList.iterator();
            while (sessionIterator.hasNext()) {
                Session session = sessionIterator.next();
                if (userToken.equals(session.getToken())) {
                    if (session.getExpireDate().isAfter(LocalDateTime.now())) {
                        return session;
                    }
                    sessionIterator.remove();
                }
            }
        }
        return null;
    }

    String getHashedPassword(String salt, String password) {
        String saltPassword = password + salt;
        byte[] originalString = saltPassword.getBytes();
        return DigestUtils.sha256Hex(originalString);
    }

    //For tests
    void setUserService(UserService userService) {
        this.userService = userService;
    }
}
