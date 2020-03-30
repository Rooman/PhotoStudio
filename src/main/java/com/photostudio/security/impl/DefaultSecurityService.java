package com.photostudio.security.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.exception.LoginPasswordInvalidException;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.service.UserService;
import com.photostudio.web.util.MailSender;
import com.photostudio.web.util.UtilClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class DefaultSecurityService implements SecurityService {
    private static final int DEFAULT_PASSWORD_LENGTH = 8;
    private static final Random RANDOM = new Random();

    private UserService userService;
    private MailSender mailSender;

    private List<Session> sessionList = new CopyOnWriteArrayList<>();


    public DefaultSecurityService() {
        this(ServiceLocator.getService(UserService.class), ServiceLocator.getService(MailSender.class));
    }

    // for test purpose
    DefaultSecurityService(UserService userService, MailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Override
    public Session login(String login, String password) {
        log.info("Connect user by login: {}", login);
        User user = userService.getUserByLogin(login);
        if (user != null) {
            String hashedPassword = UtilClass.getHashedString(password, user.getSalt());
            String actualPassword = user.getPasswordHash();
            if (!hashedPassword.equals(actualPassword)) {
                log.error("Login/password invalid for user with login: {}", login);
                throw new LoginPasswordInvalidException("Login/password invalid for user with login: " + login);
            }
            String userToken = UUID.randomUUID().toString();
            return addNewSession(user, userToken);
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
        if (userToken != null) {
            Iterator<Session> sessionIterator = sessionList.iterator();
            while (sessionIterator.hasNext()) {
                Session session = sessionIterator.next();
                String sessionToken = session.getToken();
                if (userToken.equals(sessionToken)) {
                    if (session.getExpireDate().isAfter(LocalDateTime.now())) {
                        return session;
                    } else {
                        log.debug("Session with token {} is expired. Removing session from the sessionList", sessionToken);
                        sessionList.remove(session);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void register(User user) {
        // generate credentials
        String randomPassword = generatePassword(DEFAULT_PASSWORD_LENGTH);
        String randomSalt = UUID.randomUUID().toString();
        String passwordHash = UtilClass.getHashedString(randomPassword, randomSalt);

        user.setSalt(randomSalt);
        user.setPasswordHash(passwordHash);

        // save user
        userService.add(user);

        // send email
        mailSender.send("Your account by Miari Fotografie", "Dear Customer, your account is activated. " +
                "You can log in using password " + randomPassword, user.getEmail());
    }

    @Override
    public boolean isOldPassword(String inputOldPassword, User user) {
        String inputOldPasswordHash = UtilClass.getHashedString(inputOldPassword, user.getSalt());

        return inputOldPasswordHash.equals(user.getPasswordHash());
    }

    String generatePassword(int count) {
        final int A_LETTER_CODE = 65;
        final int Z_LETTER_CODE = 90;
        if (count == 0) {
            throw new IllegalArgumentException("Password length cannot be 0");
        }

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < count; i++) {
            boolean isDigit = RANDOM.nextBoolean();
            if (isDigit) {
                // generate digit [0-9]
                int digit = RANDOM.nextInt(10);
                password.append(digit);
            } else {
                // generate letter [A-Z]
                int letterCode = RANDOM.nextInt(Z_LETTER_CODE - A_LETTER_CODE + 1) + A_LETTER_CODE;
                char letter = (char) letterCode;
                password.append(letter);
            }
        }
        return password.toString();
    }

    //For tests
    void setUserService(UserService userService) {
        this.userService = userService;
    }

    //Extracted this logic into separate method for test purposes
    Session addNewSession(User user, String userToken) {
        String login = user.getEmail() != null ? user.getEmail() : user.getPhoneNumber();
        Session session = Session.builder().user(user)
                .token(userToken).expireDate(LocalDateTime.now().plusHours(2)).build();
        sessionList.add(session);
        log.info("User with login: {} is logged in", login);
        return session;
    }
}
