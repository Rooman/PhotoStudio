package com.photostudio.dao.jdbc;

import com.photostudio.entity.User;
import com.photostudio.util.PropertyReader;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserDaoITest {
    Properties properties = new PropertyReader("application.properties").getProperties();
    DataSource dataSource = new DataSourceFactory(properties).createDataSource();
    JdbcUserDao jdbcUserDao = new JdbcUserDao(dataSource);

    @Test
    void testGetAllUsers() {
        List<User> users = jdbcUserDao.getAllUsers();

        for (User user : users) {
            assertTrue(user.getId() > 0);
            assertNotNull(user.getEmail());
            assertNotNull(user.getPasswordHash());
            assertNotNull(user.getSalt());
            assertNotNull(user.getUserRoleId());
        }
    }
}