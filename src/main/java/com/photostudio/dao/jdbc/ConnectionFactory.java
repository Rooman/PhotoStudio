package com.photostudio.dao.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionFactory implements DataSource {

    private Properties properties;

    public ConnectionFactory(Properties properties) {
        this.properties = properties;
    }

    public Connection getConnection() {
        Connection connection;
        try {
            String url = properties.getProperty("jdbc.url");
            String user = properties.getProperty("jdbc.user");
            String pass = properties.getProperty("jdbc.password");

            connection = DriverManager.getConnection(url, user, pass);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Can't connection to the database. Check your url, pass and user name", e);
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter printWriter) throws SQLException {

    }

    public void setLoginTimeout(int i) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }


    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
