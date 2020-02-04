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
        MysqlConnectionPoolDataSource mysqlConnectionPoolDataSource = new MysqlConnectionPoolDataSource();

        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.user");
        if (user != null) {
            String password = properties.getProperty("jdbc.password");

            mysqlConnectionPoolDataSource.setUser(user);
            mysqlConnectionPoolDataSource.setPassword(password);
        }
        mysqlConnectionPoolDataSource.setUrl(url);

        return mysqlConnectionPoolDataSource;
    }
}
