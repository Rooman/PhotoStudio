package com.photostudio.web.listener;

import com.photostudio.ServiceLocator;
import com.photostudio.util.PropertyReader;
import com.photostudio.web.templater.TemplateEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Properties;

@WebListener
public class ThymeleafInitializerListener implements ServletContextListener {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Initializing thymeleaf processor");
        ServletContext servletContext = sce.getServletContext();
        Properties properties = ServiceLocator.getService(PropertyReader.class).getProperties();
        TemplateEngineFactory.configTemplate(servletContext, properties);
    }
}