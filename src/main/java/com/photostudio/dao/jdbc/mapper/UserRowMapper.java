package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.Gender;
import com.photostudio.entity.User;
import com.photostudio.entity.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setUserRoleId(UserRole.getByUserRole(resultSet.getString("roleName")));
        user.setPasswordHash(resultSet.getString("passwordHash"));
        user.setSalt(resultSet.getString("salt"));
        user.setPhoneNumber(resultSet.getInt("phoneNumber"));
        user.setGender(Gender.getByGender(resultSet.getString("genderName")));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setCountry(resultSet.getString("country"));
        user.setCity(resultSet.getString("city"));
        user.setStreet(resultSet.getString("street"));
        user.setBuildingNumber(resultSet.getInt("buildingNumber"));
        user.setZip(resultSet.getInt("zip"));

        return user;
    }
}
