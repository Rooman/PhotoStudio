package com.photostudio.web.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieManager {
    public static void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
        log.info("Add cookie: {}", cookie.getName());
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    log.info("Get cookie: {}", cookie.getName());
                    return cookie.getValue();
                }
            }
        }
        log.info("Cookies not found");
        return null;
    }
}
