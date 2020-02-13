package com.photostudio.service;

import com.photostudio.entity.user.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void register(User user, String password);

    User getUserById(long id);

    void edit(User user);

    void delete(long id);

    User getUserByLogin(String login);
}
