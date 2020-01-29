package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.Gender;
import com.photostudio.entity.User;
import com.photostudio.entity.UserRole;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRowMapperTest {

    @Test
    public void testMapRow() throws SQLException {
        //prepare
        UserRowMapper userRowMapper = new UserRowMapper();

        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockResultSet.getLong("id")).thenReturn((long) 555);
        when(mockResultSet.getString("email")).thenReturn("emile@gmail.com");
        when(mockResultSet.getString("roleName")).thenReturn("admin");
        when(mockResultSet.getString("passwordHash")).thenReturn("passwordHashUser");
        when(mockResultSet.getString("salt")).thenReturn("saltUser");
        when(mockResultSet.getInt("phoneNumber")).thenReturn(493040054);//+49 30 40054033
        when(mockResultSet.getString("genderName")).thenReturn("FEMALE");
        when(mockResultSet.getString("firstName")).thenReturn("firstNameUser");
        when(mockResultSet.getString("lastName")).thenReturn("lastNameUser");
        when(mockResultSet.getString("country")).thenReturn("Germany");
        when(mockResultSet.getString("city")).thenReturn("Berlin");
        when(mockResultSet.getString("street")).thenReturn("Krausnickstraße");
        when(mockResultSet.getInt("buildingNumber")).thenReturn(25);//"25A 3/4"
        when(mockResultSet.getInt("zipCode")).thenReturn(10178);

        //when
        User actual = userRowMapper.mapRow(mockResultSet);

        //then
        assertNotNull(actual);

        assertEquals(555, actual.getId());
        assertEquals("emile@gmail.com", actual.getEmail());
        assertEquals(UserRole.ADMIN, actual.getUserRoleId());
        assertEquals("passwordHashUser", actual.getPasswordHash());
        assertEquals("saltUser", actual.getSalt());
        assertEquals(493040054, actual.getPhoneNumber());
        assertEquals(Gender.FEMALE, actual.getGender());
        assertEquals("firstNameUser", actual.getFirstName());
        assertEquals("lastNameUser", actual.getLastName());
        assertEquals("Germany", actual.getCountry());
        assertEquals("Berlin", actual.getCity());
        assertEquals("Krausnickstraße", actual.getStreet());
        assertEquals(25, actual.getBuildingNumber());

    }
}