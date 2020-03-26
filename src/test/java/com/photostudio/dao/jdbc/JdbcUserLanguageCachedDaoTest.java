package com.photostudio.dao.jdbc;

import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.user.UserLanguage;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class JdbcUserLanguageCachedDaoTest {
    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcUserLanguageCachedDao jdbcUserLanguageCachedDao;

    @BeforeAll
    public static void beforeAll() throws IOException, SQLException {
        JdbcDataSource jdbcDataSource = dataSource.init();
        jdbcUserLanguageCachedDao = new JdbcUserLanguageCachedDao(jdbcDataSource);
    }

    @Test
    void getAllLanguages() {
        List<UserLanguage> languages = jdbcUserLanguageCachedDao.getAllLanguages();

        assertEquals(3, languages.size());

        assertEquals(1, languages.get(0).getId());
        assertEquals("EN", languages.get(0).getShortName());
        assertEquals("English", languages.get(0).getFullName());

        assertEquals(2, languages.get(1).getId());
        assertEquals("DE", languages.get(1).getShortName());
        assertEquals("Deutsch", languages.get(1).getFullName());

        assertEquals(3, languages.get(2).getId());
        assertEquals("RU", languages.get(2).getShortName());

    }

    @Test
    void testGetLanguageById() {
        UserLanguage userLanguage = jdbcUserLanguageCachedDao.getLanguageById(1);
        assertEquals("EN", userLanguage.getShortName());
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        dataSource.close();
    }
}