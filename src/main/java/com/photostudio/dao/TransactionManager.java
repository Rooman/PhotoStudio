package com.photostudio.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionManager {
    Connection startTransaction() throws SQLException;

    void stopTransaction() throws SQLException;

    void rollback() throws SQLException;

    void commit() throws SQLException;
}
