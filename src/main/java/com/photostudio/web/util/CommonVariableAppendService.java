package com.photostudio.web.util;

import com.photostudio.ServiceLocator;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class CommonVariableAppendService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    public void appendUser(Map<String, Object> paramsMap, HttpServletRequest request) {
        String cookieUserToken = "user-token";
        String token = new CookieManager().getCookie(request, cookieUserToken);
        if (token == null) {
            return;
        }
        LOG.info("Token is: {}", token);
        Session session = securityService.getSession(token);
        if (session == null) {
            return;
        }
        LOG.info("Session is : {}", session);
        paramsMap.put("user", session.getUser());
    }
}
