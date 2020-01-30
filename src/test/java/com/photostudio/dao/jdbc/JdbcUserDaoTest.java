package com.photostudio.dao.jdbc;

import com.photostudio.entity.User;
import com.photostudio.util.PropertyReader;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserDaoTest {
    Properties properties = new PropertyReader("application.properties").getDevProperties();
    DataSource dataSource = new ConnectionFactory(properties);
    JdbcUserDao jdbcUserDao = new JdbcUserDao(dataSource);

    @Test
    void testGetAllUsers() {
        List<User> users = jdbcUserDao.getAllUsers();

        for (User user : users) {
            assertNotNull(user.getId());
            assertNotNull(user.getEmail());
            assertNotNull(user.getPhoneNumber());
            assertNotNull(user.getFirstName());
            assertNotNull(user.getPasswordHash());
            assertNotNull(user.getSalt());
            assertNotNull(user.getCountry());
            assertNotNull(user.getCity());
            assertNotNull(user.getZip());
            assertNotNull(user.getStreet());
            assertNotNull(user.getBuildingNumber());
            assertNotNull(user.getGender());
            assertNotNull(user.getUserRoleId());
        }
    }
}