package com.photostudio.web.util;

import com.photostudio.ServiceLocator;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.cookie.CookieManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class CommonVariableAppendService {
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    public void appendUser(Map<String, Object> paramsMap, HttpServletRequest request) {
        String cookieUserToken = "user-token";

        String token = new CookieManager().getCookie(request, cookieUserToken);
        if (token == null) {
            return;
        }

        Session session = securityService.getSession(token);
        if (session == null) {
            return;
        }

        paramsMap.put("user", session.getUser());
    }
}
