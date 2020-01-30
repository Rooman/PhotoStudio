package com.photostudio.web.listener;

import com.photostudio.web.templater.TemplateEngineFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ThymeleafInitializerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing thymeleaf processor");
        ServletContext servletContext = sce.getServletContext();
        TemplateEngineFactory.configTemplate(servletContext);
    }
}