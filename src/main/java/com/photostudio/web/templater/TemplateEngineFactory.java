package com.photostudio.web.templater;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
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
        TEMPLATE_ENGINE.setTemplateResolver(templateResolver);
    }


    public static void process(String template, Map<String, Object> productsMap, Writer writer) {
        IContext context = new Context(Locale.getDefault(), productsMap);
        TEMPLATE_ENGINE.process(template, context, writer);
    }

}
