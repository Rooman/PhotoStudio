package com.photostudio.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Executor {

    public static int execUpdate(Connection connection, String sql, int parameter) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, parameter);
            preparedStatement.executeUpdate();
            int updated = preparedStatement.getUpdateCount();
            return updated;
        }
    }

    public static int execUpdate(Connection connection, String sql, long parameter) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, parameter);
            preparedStatement.executeUpdate();
            int updated = preparedStatement.getUpdateCount();
            return updated;
        }
    }
}
