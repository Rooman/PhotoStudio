
package com.photostudio.security;

import com.photostudio.security.entity.Session;

public interface SecurityService {
    Session login(String login, String password);

    Session getSession(String userToken);
}