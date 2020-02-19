package com.photostudio.util;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public class PropertyReader {
    private String path;

    public PropertyReader(String path) {
        this.path = path;
    }

    public Properties getProperties() {
        String prodEnvironment = System.getenv("environment");

        if (prodEnvironment != null && prodEnvironment.equalsIgnoreCase("PROD")) {
            return getProdProperties();
        }
        return getDevProperties();
    }

    private Properties getProdProperties() {
        try {
            Properties properties = new Properties();

            properties.setProperty("jdbc.url", System.getenv("JDBC_DATABASE_URL"));

            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Exception while were trying to get connection properties on production environment ", e);
        }
    }

    private Properties getDevProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("No properties on path " + path);
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Can't read properties file " + path, e);
        }

    }
}