package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("emile"));
        user.setUserRoleId(resultSet.getInt("userRoleId"));
        user.setPasswordHash(resultSet.getString("passwordHash"));
        user.setSalt(resultSet.getString("salt"));
        user.setPhoneNumber(resultSet.getInt("phoneNumber"));
        user.setGender(resultSet.getString("gender"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setCountry(resultSet.getString("country"));
        user.setCity(resultSet.getString("city"));
        user.setStreet(resultSet.getString("street"));
        user.setBuildingNumber(resultSet.getInt("buildingNumber"));
        user.setZipCode(resultSet.getInt("zipCode"));

        return user;
    }
}
