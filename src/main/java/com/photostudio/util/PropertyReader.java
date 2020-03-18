package com.photostudio.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

@Slf4j
public class PropertyReader {
    private String path;
    private Properties properties;

    public PropertyReader(String path) {
        this.path = path;
        properties = getAllProperties();
    }

    public String getString(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public Integer getInt(String propertyName) {
        String strProperty = getString(propertyName);

        return strProperty == null ? null : Integer.valueOf(strProperty);
    }

    public Properties getAllProperties() {
        log.info("Try get properties");
        String prodEnvironment = System.getenv("environment");
        if (prodEnvironment != null && prodEnvironment.equalsIgnoreCase("PROD")) {
            log.info("Type of properties is production");
            return getProdProperties();
        }
        log.info("Type of properties is development");
        return getDevProperties();
    }

    private Properties getProdProperties() {
        try {
            Properties properties = getDevProperties();

            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            properties.setProperty("jdbc.url", dbUrl);
            log.debug("Set jdbc.url: {}", dbUrl);

            String dirPhoto = System.getenv("dir_photo");
            properties.setProperty("dir.photo", dirPhoto);
            log.debug("Set dir.photo: {}", dirPhoto);

            String adminMailPassword = System.getenv("ADMIN_MAIL_PASSWORD");
            properties.setProperty("mail.admin.password", adminMailPassword);
            log.debug("Set admin.mail.password: {}", adminMailPassword);

            return properties;
        } catch (Exception e) {
            log.error("Error while were trying to get connection properties on production environment", e);
            throw new RuntimeException("Exception while were trying to get connection properties on production environment", e);
        }
    }

    private Properties getDevProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("No properties on path " + path);
            }
            properties.load(inputStream);
            log.debug("Read properties from path: {}", path);
            return properties;
        } catch (IOException e) {
            log.error("Can't read properties file: {} ", path, e);
            throw new RuntimeException("Can't read properties file " + path, e);
        }

    }
}
