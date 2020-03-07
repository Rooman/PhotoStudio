package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.LoginPasswordInvalidException;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserDaoITest {

    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcDataSource jdbcDataSource;

    @BeforeAll
    public static void addTestData() throws SQLException {
        jdbcDataSource = dataSource.init();
        dataSource.runScript("db/data.sql");
    }


    @Test
    void testGetAllUsers() {
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcDataSource);
        List<User> users = jdbcUserDao.getAllUsers();

        for (User user : users) {
            assertTrue(user.getId() > 0);
            assertNotNull(user.getEmail());
            assertNotNull(user.getPasswordHash());
            assertNotNull(user.getSalt());
            assertNotNull(user.getUserRole());
        }
    }

    @Test
    public void testGetUserByLoginPhoneNumber() {
        //prepare
        User expectedUser = new User();
        expectedUser.setAddress("Qwerty 1234C");
        expectedUser.setCity("Kyiv");
        expectedUser.setCountry("Ukraine");
        expectedUser.setEmail("mymail@d.com");
        expectedUser.setFirstName("Piter");
        expectedUser.setLastName("Lol");
        expectedUser.setPhoneNumber("380731234567");
        expectedUser.setZip(12345);
        expectedUser.setId(1);
        expectedUser.setPasswordHash("8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257");
        expectedUser.setSalt("3d47ccde-5b58-4c7b-a84c-28c27d566f8e");
        expectedUser.setUserRole(UserRole.ADMIN);
        expectedUser.setTitle("Mr.");
        expectedUser.setAdditionalInfo("Friendly");

        //when
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcDataSource);
        User actualUser = jdbcUserDao.getByLogin("380731234567");

        //then
        assertNotNull(actualUser);
        assertEquals(expectedUser.getPasswordHash(), actualUser.getPasswordHash());
        assertEquals(expectedUser.getSalt(), actualUser.getSalt());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getAddress(), actualUser.getAddress());
        assertEquals(expectedUser.getCity(), actualUser.getCity());
        assertEquals(expectedUser.getCountry(), actualUser.getCountry());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(expectedUser.getUserRole(), actualUser.getUserRole());
        assertEquals(expectedUser.getZip(), actualUser.getZip());
        assertEquals(expectedUser.getTitle(), actualUser.getTitle());
        assertEquals(expectedUser.getAdditionalInfo(), actualUser.getAdditionalInfo());
    }

    @Test
    public void testGetUserByLoginEmail() {
        //prepare
        User expectedUser = new User();
        expectedUser.setEmail("mymail2@d.com");
        expectedUser.setId(2);
        expectedUser.setPasswordHash("93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce");
        expectedUser.setSalt("fd75bf19-948d-4b3e-b7c6-42dbace77271");
        expectedUser.setUserRole(UserRole.USER);

        //when
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcDataSource);
        User actualUser = jdbcUserDao.getByLogin("mymail2@d.com");

        //then
        assertNotNull(actualUser);
        assertEquals(expectedUser.getPasswordHash(), actualUser.getPasswordHash());
        assertEquals(expectedUser.getSalt(), actualUser.getSalt());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getAddress(), actualUser.getAddress());
        assertEquals(expectedUser.getCity(), actualUser.getCity());
        assertEquals(expectedUser.getCountry(), actualUser.getCountry());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(expectedUser.getUserRole(), actualUser.getUserRole());
        assertEquals(expectedUser.getZip(), actualUser.getZip());
        assertEquals(expectedUser.getTitle(), actualUser.getTitle());
        assertEquals(expectedUser.getAdditionalInfo(), actualUser.getAdditionalInfo());
    }

    @Test
    public void testGetUserByLoginIncorrectEmail() {
        //when
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcDataSource);

        //then
        Assertions.assertThrows(LoginPasswordInvalidException.class, () -> {
            jdbcUserDao.getByLogin("mymail2564@d.com");
        });
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        dataSource.close();
    }


}