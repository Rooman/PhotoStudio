package com.photostudio.dao.jdbc;

import com.photostudio.util.PropertyReader;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryITest {

    @Test
    public void testGetConnection() throws SQLException {

        PropertyReader propertyReader = new PropertyReader("application.properties");
        Properties properties = propertyReader.getProperties();
        ConnectionFactory connectionFactory = new ConnectionFactory(properties);
        assertFalse(connectionFactory.getConnection().isClosed());
    }

}