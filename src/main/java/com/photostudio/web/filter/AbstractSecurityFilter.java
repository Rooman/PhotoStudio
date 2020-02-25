package com.photostudio.web.filter;


import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
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
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        boolean isAuth = false;

        String token = cookieManager.getCookie(request,"user-token");
        Session session = securityService.getSession(token);
        if (session != null) {
            UserRole userRole = session.getUser().getUserRole();
            if (getAcceptedRoles().contains(userRole)) {
                isAuth = true;
                User user = session.getUser();
                request.setAttribute("user", user);
                request.setAttribute("session", session);
                chain.doFilter(request, response);
            }
        }

        if (!isAuth) {
           // RequestDispatcher dispatcher =

        //} else if (userRole == UserRole.USER){
        //    resp.sendRedirect("/access_denied");
        //} else {
            chain.doFilter(request, response);
          //  response.sendRedirect("/login");
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