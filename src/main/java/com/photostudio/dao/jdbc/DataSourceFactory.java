package com.photostudio.dao.jdbc;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.photostudio.util.PropertyReader;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceFactory {
    private PropertyReader propertyReader;

    public DataSourceFactory(PropertyReader propertyReader) {
        this.propertyReader = propertyReader;
    }

    public DataSource createDataSource() {
        MysqlConnectionPoolDataSource mysqlConnectionPoolDataSource = new MysqlConnectionPoolDataSource();

        String url = propertyReader.getString("jdbc.url");
        String user = propertyReader.getString("jdbc.user");
        if (user != null) {
            String password = propertyReader.getString("jdbc.password");

            mysqlConnectionPoolDataSource.setUser(user);
            mysqlConnectionPoolDataSource.setPassword(password);
        }
        mysqlConnectionPoolDataSource.setUrl(url);

        return mysqlConnectionPoolDataSource;
    }
}
