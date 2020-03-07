package com.photostudio.dao.jdbc.testUtils;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class TestDataSource {
    private Connection connection;

    public JdbcDataSource init() {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:photostudio;MODE=MySQL");
        jdbcDataSource.setUser("h2");
        jdbcDataSource.setPassword("h2");
        try {
            connection = jdbcDataSource.getConnection();
            runScript("db/migration/V1_0__initial_schema.sql");
            runScript("db/migration/V1_1__user_phone_type.sql");
            runScript("db/migration/V1_2__drop_User_genderId_column.sql");
            runScript("db/migration/V1_3__drop_gender_table.sql");
            runScript("db/migration/V1_4__add_title_addinfo_columns_to_User.sql");
            runScript("db/migration/V1_5__insert_UserRole.sql");
            runScript("db/migration/V1_6__insert_OrderStatus.sql");
            runScript("db/migration/V1_7__insert_PhotoStatus.sql");

        } catch (SQLException e) {

        }

        return jdbcDataSource;
    }

    public void runScript(String path) throws SQLException {
        try {
            FileReader fileData = new FileReader(getClass().getClassLoader().getResource(path).getFile());
            RunScript.execute(connection, fileData);
            fileData.close();
        } catch (IOException e) {

        }

    }

    public int getResult(String sqlQuery) {
        int result = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error in the getResult:", ex);
        }
        return result;
    }

    public void close() throws SQLException {
        connection.close();
    }
}







