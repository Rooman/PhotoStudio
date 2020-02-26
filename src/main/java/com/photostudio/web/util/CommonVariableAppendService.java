package com.photostudio.web.util;

import com.photostudio.security.entity.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class CommonVariableAppendService {
    public static void appendUser(Map<String, Object> paramsMap, HttpServletRequest request) {
        Session session = (Session) request.getAttribute("session");
        if (session != null) {
            paramsMap.put("user", session.getUser());
        }
    }
}
