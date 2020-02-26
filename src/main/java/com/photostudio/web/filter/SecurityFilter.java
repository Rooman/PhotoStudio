package com.photostudio.web.filter;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.web.util.CookieManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.photostudio.entity.user.UserRole.*;


@Slf4j
public class SecurityFilter implements Filter {
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);

    private Map<String, UserRole> urlToRoleMap = new HashMap<>();

    private List<String> excludedUrls;

    @Override
    public void init(FilterConfig filterConfig) {
        String excludedUrlsStr = filterConfig.getInitParameter("excludedUrls");
        this.excludedUrls = Arrays.asList(excludedUrlsStr.split(","));

        urlToRoleMap.put("/logout", USER);
        urlToRoleMap.put("/orders", USER);
        urlToRoleMap.put("/user", USER);
        // Optional, included by default
        urlToRoleMap.put("/admin/users", ADMIN);
        urlToRoleMap.put("/admin", ADMIN);

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String token = CookieManager.getCookie(request, "user-token");
        Session session = securityService.getSession(token);
        request.setAttribute("session", session);

        String servletPath = request.getServletPath();
        String currentUserLogin = session == null ? GUEST.getName() : session.getUser().getEmail();
        log.debug("Attempt to access url {} by user {}", servletPath, currentUserLogin);

        if (isExcludedUrl(servletPath)) {
            chain.doFilter(request, response);
            log.debug("Access to excluded url {}", servletPath);
        } else if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            UserRole requiredRole = urlToRoleMap.getOrDefault(servletPath, ADMIN);
            User currentUser = session.getUser();
            if (hasAccess(requiredRole, currentUser.getUserRole())) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/access-denied");
                log.debug("Access denied to url {} for user {}", servletPath, currentUserLogin);
            }
        }
    }

    // TODO: Add Test!!!!!!!
    private boolean isExcludedUrl(String url) {
        if (excludedUrls.contains("/") && url.isEmpty()) {
            return true;
        }

        for (String excludedUrl : excludedUrls) {
            if (excludedUrl.endsWith("/*")) {
                excludedUrl = excludedUrl.substring(0, excludedUrl.indexOf("*"));
                if (url.startsWith(excludedUrl)) {
                    return true;
                }
            } else if (excludedUrl.equalsIgnoreCase(url)) {
                return true;
            }

        }
        return false;
    }

    // TODO: Add Test!!!!!!!
    private boolean hasAccess(UserRole required, UserRole actual) {
        if (required == actual) {
            return true;
        }

        if (required == USER && actual == ADMIN) {
            return true;
        }

        return false;
    }
}
