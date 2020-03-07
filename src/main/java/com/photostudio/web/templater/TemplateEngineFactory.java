package com.photostudio.web.templater;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

public class TemplateEngineFactory {
    private static TemplateEngine TEMPLATE_ENGINE = new TemplateEngine();
    private static boolean isConfigured;

    public static void configTemplate(ServletContext servletContext) {
        if (isConfigured) {
            return;
        }
        isConfigured = true;
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        TEMPLATE_ENGINE.addDialect(new Java8TimeDialect());
        TEMPLATE_ENGINE.setTemplateResolver(templateResolver);
    }


    public static void process(HttpServletRequest request, HttpServletResponse response, String template, Map<String, Object> productsMap) throws IOException {
        Locale locale = productsMap.get("lang") != null ? Locale.forLanguageTag((String) productsMap.get("language")) : Locale.getDefault();
        IContext context = new WebContext(request, response, request.getServletContext(), locale, productsMap);
        TEMPLATE_ENGINE.process(template, context, response.getWriter());
    }

}
