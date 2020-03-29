package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.user.UserLanguage;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class LanguageRowMapperTest {

    @Test
    void testMapRow() throws SQLException {

        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("shortName")).thenReturn("EN");
        when(mockResultSet.getString("fullName")).thenReturn("English");

        UserLanguage userLanguage = LanguageRowMapper.mapRow(mockResultSet);

        assertNotNull(userLanguage);

        assertEquals(1, userLanguage.getId());
        assertEquals("EN", userLanguage.getShortName());
        assertEquals("English", userLanguage.getFullName());
    }

}