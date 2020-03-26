
package com.photostudio.security;

import com.photostudio.entity.user.User;
import com.photostudio.security.entity.Session;

public interface SecurityService {
    Session login(String login, String password);

    void logout(Session session);

    Session getSession(String userToken);

    void register(User user);
}