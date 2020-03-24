package com.photostudio.web.filter;

import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.photostudio.entity.user.UserRole.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class SecurityFilterTest {
    private static final SecurityFilter SECURITY_FILTER = new SecurityFilter(null);
    private static final SecurityService securityService = mock(SecurityService.class);

    @BeforeAll
    public static void before() {
        SECURITY_FILTER.setExcludedUrls(Arrays.asList("/login", "/", "/assets/*"));

        SECURITY_FILTER.setSecurityService(securityService);
    }

    @Test
    public void testDoFilterOnExcludedUrl() throws IOException, ServletException {
        // prepare
        mockCurrentUser(USER);
        mockCurrentUser(GUEST);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServletPath()).thenReturn("/login");

        FilterChain filterChain = mock(FilterChain.class);

        // when
        SECURITY_FILTER.doFilter(httpServletRequest, null, filterChain);

        // then
        verify(filterChain).doFilter(httpServletRequest, null);
    }

    @Test
    public void testDoFilterWithNoSession() throws IOException, ServletException {
        // prepare
        mockCurrentUser(ADMIN);
        mockCurrentUser(USER);
        mockCurrentUser(GUEST);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServletPath()).thenReturn("/login");

        FilterChain filterChain = mock(FilterChain.class);

        // when
        SECURITY_FILTER.doFilter(httpServletRequest, null, filterChain);

        // then
        verify(filterChain).doFilter(httpServletRequest, null);
    }

    @Test
    public void testDoFilterWithSuccessAccess() throws IOException, ServletException {
        // prepare
        mockCurrentUser(ADMIN);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServletPath()).thenReturn("/admin");

        FilterChain filterChain = mock(FilterChain.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        // when
        SECURITY_FILTER.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // then
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    public void testDoFilterWithNoAuthorization() throws IOException, ServletException {
        // prepare
        mockCurrentUser(USER);
        mockCurrentUser(GUEST);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServletPath()).thenReturn("/admin");
        when(httpServletRequest.getContextPath()).thenReturn("/develop");

        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        // when
        SECURITY_FILTER.doFilter(httpServletRequest, httpServletResponse, null);

        // then
        verify(httpServletResponse).sendRedirect("/develop/access-denied");
    }

    @Test
    public void testIsExcludedUrl() {
        // success cases
        assertTrue(SECURITY_FILTER.isExcludedUrl("/login"));
        assertTrue(SECURITY_FILTER.isExcludedUrl("/assets/css/style.css"));
        assertTrue(SECURITY_FILTER.isExcludedUrl("/"));
        assertTrue(SECURITY_FILTER.isExcludedUrl(""));

        // failure cases
        assertFalse(SECURITY_FILTER.isExcludedUrl("/logout"));
        assertFalse(SECURITY_FILTER.isExcludedUrl("/order"));
        assertFalse(SECURITY_FILTER.isExcludedUrl("/orders"));
        assertFalse(SECURITY_FILTER.isExcludedUrl("/photo"));
        assertFalse(SECURITY_FILTER.isExcludedUrl("/order/forward"));
        assertFalse(SECURITY_FILTER.isExcludedUrl("/admin/users"));
        assertFalse(SECURITY_FILTER.isExcludedUrl("/order/delete"));
        assertFalse(SECURITY_FILTER.isExcludedUrl("/order/1"));
    }

    @Test
    public void testHasAccess() {
        // success cases
        assertTrue(SECURITY_FILTER.hasAccess(USER, USER));
        assertTrue(SECURITY_FILTER.hasAccess(USER, ADMIN));
        assertTrue(SECURITY_FILTER.hasAccess(ADMIN, ADMIN));

        // failure cases
        assertFalse(SECURITY_FILTER.hasAccess(ADMIN, USER));
        assertFalse(SECURITY_FILTER.hasAccess(USER, null));
        assertFalse(SECURITY_FILTER.hasAccess(null, ADMIN));
    }

    private static void mockCurrentUser(UserRole userRole) {
        User user = new User();
        user.setUserRole(userRole);
        Session userSession = Session.builder().user(user).build();

        when(securityService.getSession(any())).thenReturn(userSession);
    }
}