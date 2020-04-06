package com.photostudio.dao.jdbc;


import com.photostudio.dao.TransactionManager;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class JdbcTransactionManager implements TransactionManager {
    private DataSource dataSource;
    private Connection connection;

    public JdbcTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection startTransaction() throws SQLException {
        log.info("start transaction");
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    @Override
    public void stopTransaction() throws SQLException {
        connection.close();
    }

    @Override
    public void rollback() throws SQLException {
        log.info("rollback in transaction manager");
        connection.rollback();
    }

    @Override
    public void commit() throws SQLException {
        log.info("commit in transaction manager");
        connection.commit();
    }
}
