package com.photostudio.security;

import com.photostudio.entity.user.User;
import com.photostudio.security.entity.Session;

public interface SecurityService {
    Session login(String login, String password);

    void createAndInjectSaltAndHashedPassword(User user);
}
