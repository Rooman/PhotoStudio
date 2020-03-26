package com.photostudio.dao.jdbc.testUtils;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class TestDataSource {
    private Connection connection;

    public JdbcDataSource init() throws SQLException, IOException {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:photostudio;MODE=MySQL");
        jdbcDataSource.setUser("h2");
        jdbcDataSource.setPassword("h2");
        connection = jdbcDataSource.getConnection();

        runScript("db/migration/V1_0__initial_schema.sql");
        runScript("db/migration/V1_1__user_phone_type.sql");
        runScript("db/migration/V1_2__drop_User_genderId_column.sql");
        runScript("db/migration/V1_3__drop_gender_table.sql");
        runScript("db/migration/V1_4__add_title_addinfo_columns_to_User.sql");
        runScript("db/migration/V1_5__insert_UserRole.sql");
        runScript("db/migration/V1_6__insert_OrderStatus.sql");
        runScript("db/migration/V1_7__insert_PhotoStatus.sql");
        runScript("db/migration/V1_10__add_language.sql");
        runScript("db/migration/V1_11__add_email_templates.sql");
        return jdbcDataSource;
    }

    public void runScript(String path) throws SQLException, IOException {
        try (FileReader fileData = new FileReader(getClass().getClassLoader().getResource(path).getFile())) {
            RunScript.execute(connection, fileData);
        }
    }

    public int getResult(String sqlQuery) throws SQLException {
        int result = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        }
        return result;
    }

    public String getString(String sqlQuery) throws SQLException {
        String result = null;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
        }
        return result;
    }

    public void execUpdate(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void close() throws SQLException {
        connection.close();
    }
}







