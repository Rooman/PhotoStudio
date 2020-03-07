package com.photostudio.dao.jdbc;

import com.photostudio.dao.OrderStatusDao;
import com.photostudio.entity.order.OrderStatus;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JdbcOrderStatusCachedDao implements OrderStatusDao {
    private static final String GET_ORDER_STATUSES = "SELECT id, statusName FROM OrderStatus";

    private Map<OrderStatus, Integer> orderStatusCache;
    private DataSource dataSource;

    public JdbcOrderStatusCachedDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.orderStatusCache = getOrderStatuses();
    }

    @Override
    public int getOrderStatusIdByStatusName(OrderStatus status) {
        return orderStatusCache.get(status);
    }

    private Map<OrderStatus, Integer> getOrderStatuses() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_STATUSES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            Map<OrderStatus, Integer> orderStatusMap = new HashMap<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                OrderStatus orderStatus = OrderStatus.getOrderStatus(resultSet.getString("statusName"));
                orderStatusMap.put(orderStatus, id);
            }
            return orderStatusMap;
        } catch (SQLException e) {
            log.error("Get order status cache error", e);
            throw new RuntimeException("Get order status cache error", e);
        }
    }
}
