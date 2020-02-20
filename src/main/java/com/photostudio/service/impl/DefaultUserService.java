package com.photostudio.service.impl;

import com.photostudio.dao.UserDao;
import com.photostudio.entity.user.User;
import com.photostudio.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultUserService implements UserService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userDao.getAllUsers();
        LOG.debug("Started service get all users from DB: {}", allUsers);
        return allUsers;
    }

    @Override
    public void add(User user) {
        LOG.debug("Started service add user: {} to DB", user);
        userDao.add(user);
    }

    @Override
    public User getUserById(long id) {
        LOG.debug("Started service get user by id : {} from DB", id);
        return userDao.getUserById(id);
    }

    @Override
    public void edit(User user) {
        LOG.debug("Started service edit user: {}", user);
        userDao.edit(user);
    }

    @Override
    public void delete(long id) {
        LOG.debug("Started service delete user with id: {} from DB", id);
        userDao.delete(id);
    }

    @Override
    public User getUserByLogin(String login) {
        LOG.debug("Started service get user by login: {} from DB", login);
        return userDao.getByLogin(login);
    }
}