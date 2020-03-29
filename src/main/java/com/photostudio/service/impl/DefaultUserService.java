package com.photostudio.service.impl;

import com.photostudio.dao.UserDao;
import com.photostudio.entity.user.User;
import com.photostudio.service.UserService;
import com.photostudio.web.util.UtilClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Slf4j
public class DefaultUserService implements UserService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userDao.getAllUsers();
        LOG.info("Started service get all users from DB");
        return allUsers;
    }

    @Override
    public void add(User user) {
        LOG.info("Started service add user to DB");
        userDao.add(user);
    }

    @Override
    public User getUserById(long id) {
        LOG.info("Started service get user by id from DB");
        return userDao.getUserById(id);
    }

    @Override
    public void edit(User user) {
        LOG.info("Started service edit user");
        userDao.edit(user);
    }

    @Override
    public void delete(long id) {
        LOG.info("Started service delete user with id from DB");
        userDao.delete(id);
    }

    @Override
    public User getUserByLogin(String login) {
        LOG.info("Started service get user by login from DB");
        return userDao.getByLogin(login);
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Started service get user by email from DB");
        return userDao.getByEmail(email);
    }

    @Override
    public User getUserByOrderId(int orderId) {
        log.info("Started service get user by order from DB");
        return userDao.getByOrderId(orderId);
    }

    @Override
    public void changeUserPassword(User user, String newPassword) {
        log.info("Started service get change user password");
        String randomSalt = UUID.randomUUID().toString();
        String newPasswordHash = UtilClass.getHashedString(newPassword, randomSalt);
        user.setSalt(randomSalt);
        user.setPasswordHash(newPasswordHash);
        userDao.edit(user);
    }
}