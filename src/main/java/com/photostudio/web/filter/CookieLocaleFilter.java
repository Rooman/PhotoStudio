package com.photostudio.web.filter;

import com.photostudio.web.util.CookieManager;
import com.photostudio.web.util.SupportedLocale;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CookieLocaleFilter", urlPatterns = {"/*"})
public class CookieLocaleFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String paramLang = req.getParameter("lang");

        SupportedLocale currentLocale;

        if (isPresent(paramLang)) {
            currentLocale = SupportedLocale.findByName(paramLang);
            CookieManager.addCookie(res, "lang", currentLocale.getName());
        } else {
            String cookieLang = CookieManager.getCookie(req, "lang");
            currentLocale = SupportedLocale.findByName(paramLang);
            res.setLocale(SupportedLocale.findByName(cookieLang).getLocale());
        }

        req.setAttribute("currentLocale", currentLocale);
        chain.doFilter(request, response);
    }

    private boolean isPresent(String paramLang) {
        return paramLang != null && !paramLang.isEmpty();
    }

}
