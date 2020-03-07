package com.photostudio.web.util;

import com.photostudio.security.entity.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CommonVariableAppendService {
    public static void appendUser(Map<String, Object> paramsMap, HttpServletRequest request) {
        Session session = (Session) request.getAttribute("session");
        if (session != null) {
            paramsMap.put("user", session.getUser());
        }
    }

    public static void appendLang(Map<String, Object> paramsMap, HttpServletRequest request, HttpServletResponse response) {
        String lang = CookieManager.getCookie(request, "lang");
        if (lang != null) {
            paramsMap.put("language", lang);
            response.setLocale(Locale.forLanguageTag(lang));
        } else {
            paramsMap.put("language", "en");
            response.setLocale(Locale.forLanguageTag("en"));
        }
    }
}
