package com.photostudio.web.filter;

import com.photostudio.web.util.CookieManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@WebFilter(filterName = "CookieLocaleFilter", urlPatterns = {"/*"})
public class CookieLocaleFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String cookieLang = CookieManager.getCookie(req, "lang");
        String paramLang = req.getParameter("lang");

        if (paramLang != null) {
            if (!paramLang.equals(cookieLang)) {
                CookieManager.addCookie(res, "lang", paramLang);
                res.setLocale(Locale.forLanguageTag(paramLang));
            }
        } else if (cookieLang != null) {
            res.setLocale(Locale.forLanguageTag(cookieLang));
        } else {
            CookieManager.addCookie(res, "lang", "eng");
            res.setLocale(Locale.forLanguageTag("eng"));
        }

        chain.doFilter(request, response);
    }
}
