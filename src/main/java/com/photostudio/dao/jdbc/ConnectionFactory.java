package com.photostudio.dao.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;

public class ConnectionFactory implements DataSource {

    private Properties properties;

    public ConnectionFactory(Properties properties) {
        this.properties = properties;
    }

    public Connection getConnection() {
        try {
            String url = properties.getProperty("jdbc.url");
            String user = properties.getProperty("jdbc.user");
            String pass = properties.getProperty("jdbc.password");
            Class.forName(properties.getProperty("jdbc.className"));

            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            throw new RuntimeException("Can't connect to the database. Check your url, password and user name", e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.logging.Logger getParentLogger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
