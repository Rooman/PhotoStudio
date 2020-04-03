package com.photostudio.service.impl;

import com.photostudio.dao.UserDao;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import com.photostudio.web.util.UtilClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class DefaultUserService implements UserService {
    private UserDao userDao;
    private OrderService orderService;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userDao.getAllUsers();
        log.info("Started service get all users from DB");
        return allUsers;
    }

    @Override
    public void add(User user) {
        log.info("Started service add user to DB");
        userDao.add(user);
    }

    @Override
    public User getUserById(long id) {
        log.info("Started service get user by id from DB");
        return userDao.getUserById(id);
    }

    @Override
    public void edit(User user) {
        log.info("Started service edit user");
        userDao.edit(user);
    }

    @Override
    public void delete(long id) {
        log.info("Started service delete user with id from DB");
        orderService.deleteAllOrdersByUserId(id);
        userDao.delete(id);
    }

    @Override
    public User getUserByLogin(String login) {
        log.info("Started service get user by login from DB");
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
    public void changeUserPassword(long userId, String newPassword) {
        log.info("Started service get change password for user with id " + userId);
        String randomSalt = UUID.randomUUID().toString();
        String newPasswordHash = UtilClass.getHashedString(newPassword, randomSalt);
        userDao.changePassword(userId, randomSalt, newPasswordHash);
    }

    @Override
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}