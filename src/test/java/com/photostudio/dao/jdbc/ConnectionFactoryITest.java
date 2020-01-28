package com.photostudio.dao.jdbc;

import com.photostudio.util.PropertyReader;

import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryITest {
    @org.junit.jupiter.api.Test
    public void testGetConnection() throws SQLException {

        PropertyReader propertyReader = new PropertyReader("application.properties");
        Properties properties = propertyReader.getProperties();
        ConnectionFactory connectionFactory = new ConnectionFactory(properties);
        assertFalse(connectionFactory.getConnection().isClosed());
    }

}