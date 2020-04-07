package com.photostudio.dao.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class Executor {

    public static void execute(Connection connection, String sql, int parameter) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, parameter);
            log.info("Execute sql : {}", sql);
            preparedStatement.execute();
        }
    }

    public static void execUpdate(Connection connection, String sql, int parameter) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, parameter);
            preparedStatement.executeUpdate();
        }
    }

    public static void execUpdate(Connection connection, String sql, long parameter) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, parameter);
            preparedStatement.executeUpdate();
        }
    }
}
