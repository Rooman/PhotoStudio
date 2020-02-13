package com.photostudio.service.impl;

import com.photostudio.dao.UserDao;
import com.photostudio.entity.user.User;
import com.photostudio.security.SecurityService;
import com.photostudio.service.UserService;
import lombok.Setter;

import java.util.List;

@Setter
public class DefaultUserService implements UserService {
    private UserDao userDao;
    private SecurityService securityService;

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void register(User user, String password) {
        securityService.createAndInjectSaltAndHashedPassword(user, password);
        userDao.add(user);
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public void edit(User user) {
        userDao.edit(user);
    }

    @Override
    public void delete(long id) {
        userDao.delete(id);
    }

    @Override
    public User getUserByLogin(String login) {
        return userDao.getByLogin(login);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }
}