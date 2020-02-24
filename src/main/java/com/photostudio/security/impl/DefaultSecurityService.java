package com.photostudio.security.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.exception.LoginPasswordInvalidException;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultSecurityService implements SecurityService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private UserService userService = ServiceLocator.getService(UserService.class);
    private List<Session> sessionList = new CopyOnWriteArrayList<>();

    @Override
    public Session login(String login, String password) {
        LOG.info("Connect user by login: {}", login);
        User user = userService.getUserByLogin(login);
        if (user != null) {
            String hashedPassword = getHashedPassword(user.getSalt(), password);
            String actualPassword = user.getPasswordHash();
            if (!hashedPassword.equals(actualPassword)) {
                LOG.error("Login/password invalid for user with login: {}", login);
                throw new LoginPasswordInvalidException("Login/password invalid for user with login: " + login);
            }
            String userToken = UUID.randomUUID().toString();
            Session session = Session.builder().user(user)
                    .token(userToken).expireDate(LocalDateTime.now().plusHours(2)).build();
            sessionList.add(session);
            LOG.info("User with login: {} is logged in", login);
            return session;
        } else {
            LOG.error("User with login: {} not found", login);
            throw new LoginPasswordInvalidException("Login/password invalid for user" + login);
        }
    }

    @Override
    public void logout(Session session) {
        if (session != null) {
            sessionList.remove(session);
        }
    }

    @Override
    public Session getSession(String userToken) {
        Session sessionResult = null;

        for (Session session : sessionList) {
            if (session.getToken().equals(userToken)) {
                sessionResult = session;
                break;
            }
        }

        return sessionResult;
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