package com.photostudio.service.impl;

import com.photostudio.dao.UserDao;
import com.photostudio.entity.user.User;
import com.photostudio.security.SecurityService;
import com.photostudio.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DefaultUserService implements UserService {
    private UserDao userDao;
    private SecurityService securityService;

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void register(User user) {
        securityService.createAndInjectSaltAndHashedPassword(user);
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
}
