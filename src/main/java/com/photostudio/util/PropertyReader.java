package com.photostudio.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public class PropertyReader {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private String path;

    public PropertyReader(String path) {
        this.path = path;
    }

    public Properties getProperties() {
        String prodEnvironment = System.getenv("environment");
        LOG.info("System environment: {}", prodEnvironment);
        if (prodEnvironment != null && prodEnvironment.equalsIgnoreCase("PROD")) {
            LOG.info("Type of properties is production");
            return getProdProperties();
        }
        LOG.info("Type of properties is development");
        return getDevProperties();
    }

    private Properties getProdProperties() {
        try {
            Properties properties = new Properties();
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            properties.setProperty("jdbc.url", dbUrl);
            LOG.info("Set jdbc.url: {}", dbUrl);
            return properties;
        } catch (Exception e) {
            LOG.error("Error while were trying to get connection properties on production environment", e);
            throw new RuntimeException("Exception while were trying to get connection properties on production environment ");
        }
    }

    private Properties getDevProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("No properties on path " + path);
            }
            properties.load(inputStream);
            LOG.info("Read properties from path: {}", path);
            return properties;
        } catch (IOException e) {
            LOG.error("Can't read properties file: {} ", path, e);
            throw new RuntimeException("Can't read properties file " + path);
        }

    }
}
