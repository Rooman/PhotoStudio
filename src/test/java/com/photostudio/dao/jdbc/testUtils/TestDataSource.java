package com.photostudio.dao.jdbc.testUtils;


import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDataSource {
    private Connection connection;

    public JdbcDataSource init() throws SQLException, IOException {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:photostudio;MODE=MySQL");
        jdbcDataSource.setUser("h2");
        jdbcDataSource.setPassword("h2");

        connection = jdbcDataSource.getConnection();

        runScript("db/schema.sql");
        runScript("db/migration/V1_5__insert_UserRole.sql");
        runScript("db/migration/V1_6__insert_OrderStatus.sql");
        runScript("db/migration/V1_7__insert_PhotoStatus.sql");
        runScript("db/migration/V1_8__insert_admin.sql");

        return jdbcDataSource;
    }

    public void runScript(String path) throws IOException, SQLException {
        FileReader fileData = new FileReader(getClass().getClassLoader().getResource(path).getFile());
        RunScript.execute(connection, fileData);
        fileData.close();
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







