package com.photostudio.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CookieLocaleFilter", urlPatterns = {"/*"})
public class CookieLocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Cookie cookie;

        String lang = request.getParameter("lang");
        if (req.getParameter("cookieLocale") != null) {
            cookie = new Cookie("lang", req.getParameter("cookieLocale"));
        } else if (lang != null) {
            cookie = new Cookie("lang", lang);
        } else {
            cookie = new Cookie("lang", "en");
        }

        res.addCookie(cookie);

        chain.doFilter(request, response);
    }
}
