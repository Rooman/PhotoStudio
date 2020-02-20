package com.photostudio.web.filter;


import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.UserRole;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.util.CookieManager;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public abstract class AbstractSecurityFilter implements Filter {
    private SecurityService securityService;

    public AbstractSecurityFilter() {
        securityService = ServiceLocator.getService(SecurityService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        //This variables would be needed to add redirection. See comment in the end of method
        //HttpServletResponse resp = (HttpServletResponse) response;
        //boolean isAuth = false;
        //UserRole userRole;

        String token = new CookieManager().getCookie(req,"token");
        Session session = securityService.getSession(token);
        if (session != null) {
            UserRole userRole = session.getUser().getUserRole();
            if (getAcceptedRoles().contains(userRole)) {
                //isAuth = true;
                req.setAttribute("session", session);
            }
        }

        //I can add a redirection here. For example if user tries to visit admin page,
        //he can be redirected to some "access denied" warning page
        chain.doFilter(request, response);
    }

    abstract Set<UserRole> getAcceptedRoles();

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}