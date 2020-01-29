package com.photostudio.dao.jdbc;

import com.photostudio.dao.UserDao;
import com.photostudio.dao.jdbc.mapper.UserRowMapper;
import com.photostudio.entity.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements UserDao {
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    private static final String GET_ALL_USERS = "select email, phoneNumber, firstName, lastName, genderName, roleName, passwordHash, salt, country, city, zip, street, buildingNumber from photostudio.Users  inner join UserRole on Users.userRoleId=UserRole.id inner join UserGender on Users.genderId=UserGender.id;";

    private DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> getAllUsers() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USERS)) {
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException("error. Can't show all users", e);
        }
    }

    @Override
    public void add(User user) {
    }

    @Override
    public User getUserById(long id) {
        return null;
    }

    @Override
    public void edit(User user) {

    }

    @Override
    public void delete(long id) {

    }


}
