package com.photostudio.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"},
        initParams = @WebInitParam(name = "requestEncoding", value = "UTF-8"))
@Slf4j
public class EncodingFilter implements Filter {
    private String encoding;

    public void init(FilterConfig config) {
        encoding = config.getInitParameter("requestEncoding");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("Encoding filter start with encoding:{}", encoding);
        request.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }

}