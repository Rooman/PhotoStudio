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

    private static final String GET_ALL_USERS = "SELECT id,email,userRoleId,passwordHash,salt, phoneNumber, genderId,firstName,lastName,country,city,zip,street,buildingNumber FROM photostudio.Users;";
    private static final String ADD_NEW_USER = "INSERT INTO users (email,userRoleId, phoneNumber, genderId,firstName,lastName,country,city,zip,street,buildingNumber) VALUES (?, ?, ?, ?, ?,?,?, ?, ?, ?, ?)";
    private static final String EDIT_USER = "UPDATE products SET email = ?, userRoleId = ?,phoneNumber = ?,genderId = ?,firstName = ?,lastName = ?,country = ?, city = ?, zip=?,street=?, buildingNumber=? WHERE id = ?";
    private static final String GET_USER_BY_ID = "SELECT email,userRole,passwordHash,salt, phoneNumber, genderId,firstName,lastName,country,city,zip,street,buildingNumber FROM users WHERE id = ?";
    private static final String DELETE = "DELETE FROM users WHERE id = ?";
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
            preparedStatement.setInt(2, user.getUserRoleId());
            preparedStatement.setInt(3, user.getPhoneNumber());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getFirstName());
            preparedStatement.setString(6, user.getLastName());
            preparedStatement.setString(7, user.getCountry());
            preparedStatement.setString(8, user.getCity());
            preparedStatement.setInt(9, user.getZip());
            preparedStatement.setString(10, user.getStreet());
            preparedStatement.setInt(11, user.getBuildingNumber());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("error. Can't add new user to DB ", e);
        }
    }

    @Override
    public User getUserById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new RuntimeException("error.User with id: " + id + "is missing");
                }
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                if (resultSet.next()) {
                    throw new RuntimeException("More than one user with id=  " + id + " found");
                }
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("error. Can't found user where id=" + id, e);
        }
    }

    @Override
    public void edit(User user) {
        long id = user.getId();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_USER)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setInt(2, user.getUserRoleId());
            preparedStatement.setInt(3, user.getPhoneNumber());
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getFirstName());
            preparedStatement.setString(6, user.getLastName());
            preparedStatement.setString(7, user.getCountry());
            preparedStatement.setString(8, user.getCity());
            preparedStatement.setInt(9, user.getZip());
            preparedStatement.setString(10, user.getStreet());
            preparedStatement.setInt(11, user.getBuildingNumber());
            preparedStatement.setLong(12, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("error.Cannot edit the user with id " + id, e);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("error. Cannot delete the user with id: " + id, e);
        }
    }
}
