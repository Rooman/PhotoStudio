package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.user.Gender;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setUserRole(UserRole.getByUserRole(resultSet.getString("roleName")));
        user.setPasswordHash(resultSet.getString("passwordHash"));
        user.setSalt(resultSet.getString("salt"));
        user.setPhoneNumber(resultSet.getString("phoneNumber"));
        user.setGender(Gender.getByGender(resultSet.getString("genderName")));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setCountry(resultSet.getString("country"));
        user.setCity(resultSet.getString("city"));
        user.setAddress(resultSet.getString("address"));
        user.setZip(resultSet.getInt("zip"));

        return user;
    }
}
