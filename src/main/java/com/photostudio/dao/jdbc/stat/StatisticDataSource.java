package com.photostudio.dao.jdbc.stat;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Slf4j
public class StatisticDataSource implements DataSource {
    private DataSource dataSource;

    public StatisticDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        ThrowingSupplier<Connection, SQLException> supplier = () -> dataSource.getConnection();
        return obtainConnection(supplier);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        ThrowingSupplier<Connection, SQLException> supplier = () -> dataSource.getConnection(username, password);
        return obtainConnection(supplier);
    }

    @SneakyThrows
    private Connection obtainConnection(ThrowingSupplier<Connection, SQLException> supplier) {
        long start = System.nanoTime();
        Connection connection = supplier.get();
        long end = System.nanoTime();
        StatisticsVo statisticsVo = StatisticsVoThreadHandler.createStatisticVo();
        statisticsVo.setOpenConnectionTime(end - start);

        return new StatisticsConnection(connection);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        dataSource.setLogWriter(printWriter);
    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
        dataSource.setLoginTimeout(i);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        return dataSource.unwrap(aClass);
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return dataSource.isWrapperFor(aClass);
    }
}
