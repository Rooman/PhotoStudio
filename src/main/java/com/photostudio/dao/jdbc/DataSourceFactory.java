package com.photostudio.dao.jdbc;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceFactory {

    private Properties properties;

    public DataSourceFactory(Properties properties) {
        this.properties = properties;
    }

    public DataSource createDataSource() {
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        String password = properties.getProperty("jdbc.password");

        MysqlConnectionPoolDataSource mysqlConnectionPoolDataSource = new MysqlConnectionPoolDataSource();
        mysqlConnectionPoolDataSource.setUrl(url);
        mysqlConnectionPoolDataSource.setUser(user);
        mysqlConnectionPoolDataSource.setPassword(password);
        return mysqlConnectionPoolDataSource;
    }
}
