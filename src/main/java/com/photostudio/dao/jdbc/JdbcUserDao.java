package com.photostudio.dao.jdbc;

import com.photostudio.dao.UserDao;
import com.photostudio.dao.jdbc.mapper.UserRowMapper;
import com.photostudio.entity.user.User;
import com.photostudio.exception.LoginPasswordInvalidException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements UserDao {
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    private static final String GET_ALL_USERS = "SELECT Users.id, email, phoneNumber, firstName," +
            " lastName, genderName, roleName, passwordHash, salt, country, city, zip," +
            " address FROM Users " +
            " INNER JOIN UserRole ON Users.userRoleId=UserRole.id" +
            " LEFT JOIN UserGender ON Users.genderId=UserGender.id;";

    private static final String ADD_NEW_USER = "INSERT INTO photostudio.Users (email,phoneNumber," +
            "firstName,lastName,genderId,userRoleId,passwordHash,salt, country,city,zip,address) " +
            "VALUES (?,?,?,?,(SELECT id FROM UserGender WHERE genderName=?)," +
            "( SELECT id FROM UserRole WHERE roleName ='user'),?,?,?,?,?,?,?)";

    private static final String GET_USER_BY_LOGIN = "SELECT u.id id, " +
            "u.email email, " +
            "u.phoneNumber, " +
            "u.firstName firstName, " +
            "u.lastName lastName, " +
            "ug.genderName genderName, " +
            "ur.roleName roleName, " +
            "u.passwordHash passwordHash, " +
            "u.salt salt, " +
            "u.country country, " +
            "u.city city, " +
            "u.zip zip, " +
            "u.address address " +
            "FROM Users u " +
            "JOIN UserRole ur ON u.userRoleId = ur.id " +
            "LEFT JOIN UserGender ug ON u.genderId = ug.id " +
            "WHERE ";

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

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_USER)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setLong(2, user.getPhoneNumber());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());

            preparedStatement.setObject(5, user.getGender());
            preparedStatement.setString(6, user.getPasswordHash());
            preparedStatement.setString(7, user.getSalt());
            preparedStatement.setString(8, user.getCountry());
            preparedStatement.setString(9, user.getCity());
            preparedStatement.setInt(10, user.getZip());
            preparedStatement.setString(11, user.getAddress());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("error. Can't add new user to DB ", e);
        }
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

    @Override
    public User getByLogin(String login) {
        String resultQuery;
        if (login.contains("@")) {
            resultQuery = GET_USER_BY_LOGIN + "u.email=?";
        } else {
            resultQuery = GET_USER_BY_LOGIN + "u.phoneNumber=?";
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resultQuery)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new LoginPasswordInvalidException("No user with login = " + login + " found");
                }
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                if (resultSet.next()) {
                    throw new RuntimeException("More then one user found");
                }
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Get user by login error", e);
        }
    }
}
