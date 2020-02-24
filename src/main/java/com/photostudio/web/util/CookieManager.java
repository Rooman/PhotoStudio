package com.photostudio.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
        LOG.info("Add cookie: {}", cookie.getName());
    }

    public String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    LOG.info("Get cookie: {}", cookie.getName());
                    return cookie.getValue();
                }
            }
        }
        LOG.info("Cookies not found");
        return null;
    }

}
