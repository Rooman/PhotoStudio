package com.photostudio.dao.jdbc;

import com.photostudio.dao.UserDao;
import com.photostudio.dao.UserLanguageDao;
import com.photostudio.dao.jdbc.mapper.UserRowMapper;
import com.photostudio.entity.user.User;
import com.photostudio.exception.GetUserByEmailException;
import com.photostudio.exception.LoginPasswordInvalidException;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcUserDao implements UserDao {

    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    private static final String GET_ALL_USERS = "SELECT Users.id, email, phoneNumber, firstName," +
            " lastName, title, roleName, passwordHash, salt, country, city, zip," +
            " address, additionalInfo, langId FROM Users " +
            " INNER JOIN UserRole ON Users.userRoleId=UserRole.id;";

    private static final String ADD_NEW_USER = "INSERT INTO Users (email,phoneNumber," +
            "firstName,lastName, title, userRoleId,passwordHash,salt, country,city,zip,address,additionalInfo,langId) " +
            "VALUES (?,?,?,?,?," +
            "( SELECT id FROM UserRole WHERE roleName ='User'),?,?,?,?,?,?,?,?)";

    private static final String DELETE_USER = "DELETE FROM Users WHERE id=?;";

    private static final String GET_USER_BY_PARAMS = "SELECT u.id id, " +
            "u.email email, " +
            "u.phoneNumber, " +
            "u.firstName firstName, " +
            "u.lastName lastName, " +
            "ur.roleName roleName, " +
            "u.passwordHash passwordHash, " +
            "u.salt salt, " +
            "u.country country, " +
            "u.city city, " +
            "u.zip zip, " +
            "u.title title, " +
            "u.additionalInfo additionalInfo, " +
            "u.address address, " +
            "u.langId langId " +
            "FROM Users u " +
            "JOIN UserRole ur ON u.userRoleId = ur.id " +
            "WHERE ";

    private static final String GET_BY_ID = "SELECT u.id id, u.email email,u.phoneNumber phoneNumber," +
            " u.firstName firstName, u.lastName lastName, ur.roleName roleName, " +
            " u.title title, u.additionalInfo additionalInfo, " +
            " u.passwordHash passwordHash, u.salt salt, u.country country," +
            " u.city city, u.zip zip, u.address address, u.langId langId  FROM  Users u " +
            "INNER JOIN UserRole ur ON u.userRoleId=ur.id " +
            "WHERE u.id=?;";
    private static final String EDIT_USER = "UPDATE Users u " +
            "SET " +
            "    u.email = ?," +
            "    u.phoneNumber = ?," +
            "    u.firstName = ?," +
            "    u.lastName = ?," +
            "    u.country = ?," +
            "    u.city = ?," +
            "    u.zip = ?," +
            "    u.title = ?," +
            "    u.additionalInfo = ?," +
            "    u.address = ?," +
            "    u.salt = ?," +
            "    u.passwordHash = ?," +
            "    u.langId = ? " +
            "WHERE" +
            "    u.id = ?;";
    private static final String GET_USER_BY_ORDER = "SELECT u.id id, " +
            "u.email email, " +
            "u.phoneNumber, " +
            "u.firstName firstName, " +
            "u.lastName lastName, " +
            "ur.roleName roleName, " +
            "u.passwordHash passwordHash, " +
            "u.salt salt, " +
            "u.country country, " +
            "u.city city, " +
            "u.zip zip, " +
            "u.title title, " +
            "u.additionalInfo additionalInfo, " +
            "u.address address, " +
            "u.langId langId " +
            "FROM Orders o " +
            "JOIN Users u ON o.userId = u.id " +
            "JOIN UserRole ur ON u.userRoleId = ur.id " +
            "WHERE o.id = ?";


    private DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Get all users from DB");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USERS)) {
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                users.add(user);
            }
            log.info("Get: {} users from DB", users.size());
            log.debug("Get users: {}", users);
            return users;
        } catch (SQLException e) {
            log.error("An exception occurred while trying to get all users", e);
            throw new RuntimeException("Can't show all users", e);
        }
    }

    @Override
    public void add(User user) {
        log.info("Add user to DB");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_USER)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPhoneNumber());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getTitle());
            preparedStatement.setString(6, user.getPasswordHash());
            preparedStatement.setString(7, user.getSalt());
            preparedStatement.setString(8, user.getCountry());
            preparedStatement.setString(9, user.getCity());
            preparedStatement.setInt(10, user.getZip());
            preparedStatement.setString(11, user.getAddress());
            preparedStatement.setString(12, user.getAdditionalInfo());
            preparedStatement.setInt(13, user.getLangId());
            preparedStatement.executeUpdate();
            log.info("Adding user to DB is completed");
            log.debug("Add user: {} to DB", user);
        } catch (SQLException e) {
            log.error("An exception occurred while trying to add user: {} DB", user, e);
            throw new RuntimeException("Can't add new user to DB ", e);
        }
    }

    @Override
    public User getUserById(long id) {
        log.info("Get user by id: {}", id);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new RuntimeException("User with id= " + id + "is missing");
                }
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                if (resultSet.next()) {
                    throw new RuntimeException("More than one users found");
                }
                log.info("Getting user by id: {} is completed", id);
                log.debug("Get user: {} by id: {}", user, id);
                return user;
            }
        } catch (SQLException e) {
            log.error("An exception occurred while trying to get user by id: {}", id, e);
            throw new RuntimeException("Can't found user where id=" + id, e);
        }
    }

    @Override
    public void edit(User user) {
        log.info("Edit user in DB");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EDIT_USER)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPhoneNumber());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getCountry());
            preparedStatement.setString(6, user.getCity());
            preparedStatement.setInt(7, user.getZip());
            preparedStatement.setString(8, user.getTitle());
            preparedStatement.setString(9, user.getAdditionalInfo());
            preparedStatement.setString(10, user.getAddress());
            preparedStatement.setString(11, user.getSalt());
            preparedStatement.setString(12, user.getPasswordHash());
            preparedStatement.setInt(13, user.getLangId());
            preparedStatement.setLong(14, user.getId());
            preparedStatement.executeUpdate();

            log.debug("User {} was edited", user);
        } catch (SQLException e) {
            log.error("Can't edit user", e);
            throw new RuntimeException("Can't edit user", e);
        }
    }

    @Override
    public void delete(long id) {
        log.info("Delete user by id: {}", id);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            log.info("Deleting user by id: {} is completed", id);
        } catch (SQLException e) {
            log.error("An exception occurred while trying to delete user by id: {}", id, e);
            throw new RuntimeException("Can't remove user", e);
        }
    }

    @Override
    public User getByLogin(String login) {
        log.info("Get user by login: {}", login);
        String resultQuery;
        if (login.contains("@")) {
            resultQuery = GET_USER_BY_PARAMS + "u.email=?";
        } else {
            resultQuery = GET_USER_BY_PARAMS + "u.phoneNumber=?";
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resultQuery)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    log.error("No user with login: {}", login);
                    throw new LoginPasswordInvalidException("No user with login = " + login + " found");
                }
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                if (resultSet.next()) {
                    log.error("Users with login: {} is several", login);
                    throw new RuntimeException("More then one user found");
                }
                log.info("Getting user by login: {} is completed", login);
                log.debug("Get user: {} by login: {}", user, login);
                return user;
            }
        } catch (SQLException e) {
            log.error("An exception occurred while trying to get user with login: {}", login, e);
            throw new RuntimeException("Get user by login error", e);
        }
    }

    @Override
    public User getByEmail(String email) {
        log.info("Get user by email: {}", email);
        String resultQuery = GET_USER_BY_PARAMS + "u.email=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resultQuery)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    log.error("No user with email: {}", email);
                    throw new GetUserByEmailException("No user with email = " + email + " found");
                }
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                if (resultSet.next()) {
                    log.error("Users with email: {} is several", email);
                    throw new RuntimeException("More then one user found");
                }
                log.info("Getting user by email: {} is completed", email);
                log.debug("Get user: {} by email: {}", user, email);
                return user;
            }
        } catch (SQLException e) {
            log.error("An exception occurred while trying to get user with email: {}", email, e);
            throw new RuntimeException("Get user by email error", e);
        }
    }

    @Override
    public User getByOrderId(int orderId) {
        log.info("Get user by orderId: {}", orderId);
        String resultQuery = GET_USER_BY_ORDER;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resultQuery)) {
            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = USER_ROW_MAPPER.mapRow(resultSet);

                    log.info("Getting user by orderId: {} is completed", orderId);
                    log.debug("Get user: {} by orderId: {}", user, orderId);
                    return user;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            log.error("An exception occurred while trying to get user by orderId: {}", orderId, e);
            throw new RuntimeException("Get user by orderId error", e);
        }
    }
}