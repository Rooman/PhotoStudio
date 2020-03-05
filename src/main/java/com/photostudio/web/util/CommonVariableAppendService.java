package com.photostudio.web.util;

import com.photostudio.security.entity.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

public class CommonVariableAppendService {
    public static void appendUser(Map<String, Object> paramsMap, HttpServletRequest request) {
        Session session = (Session) request.getAttribute("session");
        if (session != null) {
            paramsMap.put("user", session.getUser());
        }
    }

    public static void appendLang(Map<String, Object> paramsMap, HttpServletRequest request) {
        String lang = CookieManager.getCookie(request, "lang");
        if (lang != null) {
            paramsMap.put("language", lang);
        } else {
            paramsMap.put("language", "en");
        }
    }
}
