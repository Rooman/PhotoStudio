
package com.photostudio.service;

import com.photostudio.entity.user.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void add(User user);

    User getUserById(long id);

    void edit(User user);

    void delete(long id);

    User getUserByLogin(String login);

    User getUserByEmail(String email);

    User getUserByOrderId(int orderId);

    void changeUserPassword(User user, String newPassword);
}