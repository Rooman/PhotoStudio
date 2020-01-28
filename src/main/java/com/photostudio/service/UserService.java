package com.photostudio.service;

import com.photostudio.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void add(User user);

    User getUserById(long id);

    void edit(User user);

    void delete(long id);
}
