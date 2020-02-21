package com.photostudio.web.filter;


import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.UserRole;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.util.CookieManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public abstract class AbstractSecurityFilter implements Filter {
    private SecurityService securityService;
    private CookieManager cookieManager = new CookieManager();

    public AbstractSecurityFilter() {
        securityService = ServiceLocator.getService(SecurityService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        boolean isAuth = false;
        UserRole userRole = null;

        String token = cookieManager.getCookie(req,"user-token");
        Session session = securityService.getSession(token);
        if (session != null) {
            userRole = session.getUser().getUserRole();
            if (getAcceptedRoles().contains(userRole)) {
                isAuth = true;
                req.setAttribute("session", session);
            }
        }

        if (isAuth) {
            chain.doFilter(request, response);
        } else if (userRole == UserRole.USER){
            resp.sendRedirect("/access_denied");
        } else {
            resp.sendRedirect("/login");
        }
    }

    abstract Set<UserRole> getAcceptedRoles();

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}