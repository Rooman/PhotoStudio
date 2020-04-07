package com.photostudio.dao.jdbc;

import com.photostudio.dao.UserLanguageDao;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserLanguage;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.LoginPasswordInvalidException;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUserDaoITest {

    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcDataSource jdbcDataSource;
    private static JdbcUserDao jdbcUserDao;

    @BeforeAll
    public static void init() throws IOException, SQLException {
        jdbcDataSource = dataSource.init();
        jdbcUserDao = new JdbcUserDao(jdbcDataSource);
    }

    @BeforeEach
    public void addTestData() throws IOException, SQLException {
        dataSource.runScript("db/clear_orders.sql");
        dataSource.runScript("db/clear_users.sql");
        dataSource.runScript("db/data.sql");
    }

    private User getExpectedUser1() {
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
        expectedUser.setLangId(1);
        return expectedUser;
    }

    private User getExpectedUser2() {
        User expectedUser = new User();
        expectedUser.setEmail("mymail2@d.com");
        expectedUser.setId(2);
        expectedUser.setPasswordHash("93ba5ffe3e90c219572a823caf3d639c527f10a36d240f4a021ad4a367b7ebce");
        expectedUser.setSalt("fd75bf19-948d-4b3e-b7c6-42dbace77271");
        expectedUser.setUserRole(UserRole.USER);
        expectedUser.setLangId(1);
        return expectedUser;
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = jdbcUserDao.getAllUsers();

        for (User user : users) {
            assertTrue(user.getId() > 0);
            assertNotNull(user.getEmail());
            assertNotNull(user.getPasswordHash());
            assertNotNull(user.getSalt());
            assertNotNull(user.getUserRole());
            assertTrue(user.getId() > 0);
        }
    }

    @Test
    public void testGetUserByLoginPhoneNumber() {
        //prepare
        User expectedUser = getExpectedUser1();

        //when
        User actualUser = jdbcUserDao.getByLogin("380731234567");

        //then
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetUserByLoginEmail() {
        //prepare
        User expectedUser = getExpectedUser2();

        //when
        User actualUser = jdbcUserDao.getByLogin("mymail2@d.com");

        //then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetUserByLoginIncorrectEmail() {

        Assertions.assertThrows(LoginPasswordInvalidException.class, () -> {
            jdbcUserDao.getByLogin("mymail2564@d.com");
        });
    }

    @Test
    public void testGetUserById() {
        User expectedUser = getExpectedUser1();
        User actualUser = jdbcUserDao.getUserById(1);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testGetUserByOrderId() {
        User expectedUser = getExpectedUser2();
        User actualUser = jdbcUserDao.getByOrderId(2);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testAdd() throws SQLException {
        User expectedUser = getExpectedUser1();
        expectedUser.setId(3);
        expectedUser.setEmail("new.com");
        assertDoesNotThrow(() -> jdbcUserDao.add(expectedUser));
        int cntAdded = dataSource.getResult("SELECT count(*) FROM Users WHERE id = 3");
        assertEquals(1, cntAdded);
        int langId = dataSource.getResult("SELECT langId FROM Users WHERE id = 3");
        assertEquals(1, langId);
    }

    @Test
    public void testEdit() throws SQLException {
        User expectedUser = getExpectedUser2();
        expectedUser.setLangId(2);
        assertDoesNotThrow(() -> jdbcUserDao.edit(expectedUser));
        int langId = dataSource.getResult("SELECT langId FROM Users WHERE id = 2");
        assertEquals(2, langId);
    }

    @Test
    public void testGetAdmin() {
        User expectedUser = getExpectedUser1();
        User actualUser = jdbcUserDao.getAdmin();
        assertEquals(expectedUser, actualUser);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        dataSource.close();
    }


}