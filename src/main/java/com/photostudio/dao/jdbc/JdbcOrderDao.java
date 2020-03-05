package com.photostudio.dao.jdbc;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.OrderDao;
import com.photostudio.dao.OrderStatusDao;
import com.photostudio.dao.jdbc.mapper.OrderRowMapper;
import com.photostudio.dao.jdbc.mapper.OrderWithPhotoRowMapper;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import lombok.extern.slf4j.Slf4j;


import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Slf4j
public class JdbcOrderDao implements OrderDao {
    private static final String GET_ALL_ORDERS = "SELECT o.id id," +
            "os.statusName statusName, " +
            "o.orderDate orderDate, " +
            "u.email email, " +
            "u.phoneNumber phoneNumber, " +
            "o.comment comment " +
            "FROM Orders o " +
            "JOIN OrderStatus os ON o.statusId = os.id " +
            "JOIN Users u ON o.userId = u.id";
    private static final String GET_ORDER_BY_ID_IN_STATUS_NEW = "SELECT o.id, statusName, orderDate, email, comment, source " +
            "FROM Orders o " +
            "JOIN OrderStatus os ON o.statusId = os.id " +
            "JOIN Users u ON o.userId = u.id " +
            "LEFT JOIN OrderPhotos op ON o.id = op.orderId WHERE o.id=? and statusName='NEW'";

    private static final String DELETE_PHOTOS_BY_ORDER = "DELETE FROM OrderPhotos WHERE orderId = ?";
    private static final String DELETE_ORDER_BY_ID = "DELETE FROM Orders WHERE id = ?";

    private static final String ADD_NEW_ORDER = "INSERT INTO Orders (orderDate, statusId, userId, comment) VALUES (?, " +
            "?, ?, ?)";

    private static final OrderRowMapper ORDER_ROW_MAPPER = new OrderRowMapper();
    private static final OrderWithPhotoRowMapper ORDER_WITH_PHOTO_ROW_MAPPER = new OrderWithPhotoRowMapper();

    private DataSource dataSource;
    private OrderStatusDao orderStatusDao = ServiceLocator.getService(OrderStatusDao.class);
    public JdbcOrderDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Order> getAll() {
        log.info("Get all orders from DB");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(addSort(GET_ALL_ORDERS));
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = ORDER_ROW_MAPPER.mapRow(resultSet);
                orders.add(order);
            }
            log.info("Get: {} orders from DB", orders.size());
            log.debug("Get all orders: {}", orders);
            return orders;
        } catch (SQLException e) {
            log.error("An exception occurred while trying to get all orders", e);
            throw new RuntimeException("Error during get all orders", e);
        }
    }

    public List<Order> getOrdersByUserId(long userId) {
        log.info("Start get all orders by userId:{} from DB", userId);
        String sql = GET_ALL_ORDERS + " WHERE o.statusId!=1 AND o.userId = ?";
        sql = addSort(sql);
        log.debug("execute sql query:" + sql);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                while (resultSet.next()) {
                    Order order = ORDER_ROW_MAPPER.mapRow(resultSet);
                    orders.add(order);
                }

                log.info("Get: {} orders from DB", orders.size());
                log.debug("Get all orders: {}", orders);

                return orders;
            }
        } catch (SQLException e) {
            log.error("An exception occurred while trying to get all orders by userId", e);
            throw new RuntimeException("Error during get all orders by userId", e);
        }
    }

    //DO NOT change the order of parameters
    @Override
    public List<Order> getOrdersByParameters(FilterParameters filterParameters) {
        log.info("Get orders by parameters from DB");
        String resultWhere = getPartWhere(filterParameters);

        if (resultWhere.contains("?")) {

            String selectOrdersByParameters = addSort(GET_ALL_ORDERS + resultWhere);
            log.debug("Get orders by parameters: {} from DB", selectOrdersByParameters);
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(selectOrdersByParameters)) {
                int count = 1;
                if (filterParameters.getEmail() != null) {
                    preparedStatement.setString(count++, filterParameters.getEmail());
                }
                if (filterParameters.getFromDate() != null) {
                    preparedStatement.setTimestamp(count++, Timestamp.valueOf(filterParameters.getFromDate()));
                }
                if (filterParameters.getToDate() != null) {
                    preparedStatement.setTimestamp(count++, Timestamp.valueOf(filterParameters.getToDate()));
                }
                if (filterParameters.getOrderStatus() != null) {
                    preparedStatement.setString(count++, filterParameters.getOrderStatus().getOrderStatusName());
                }
                if (filterParameters.getPhoneNumber() != null) {
                    preparedStatement.setString(count, filterParameters.getPhoneNumber());
                }
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Order> orders = new ArrayList<>();
                    while (resultSet.next()) {
                        Order order = ORDER_ROW_MAPPER.mapRow(resultSet);
                        orders.add(order);
                    }
                    log.info("Get: {} orders by parameters", orders.size());
                    log.debug("Get orders by parameters: {}", orders);
                    return orders;
                }
            } catch (SQLException e) {
                log.error("An exception occurred while trying to get orders by parameters", e);
                throw new RuntimeException("Error during get orders by params", e);
            }
        }
        return getAll();
    }

    @Override
    public Order getOrderByIdInStatusNew(int id) {
        log.info("Started service get order by id:{} in status NEW from DB", id);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_BY_ID_IN_STATUS_NEW)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return ORDER_WITH_PHOTO_ROW_MAPPER.mapRow(resultSet);
            }

        } catch (SQLException e) {
            log.error("Get order by id:{} in status NEW error", id);
            throw new RuntimeException("Get order by id " + id + " in status NEW error", e);
        }
    }

    @Override
    public void delete(long id) {
        log.info("Delete order by id: {}", id);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statementPhotos = connection.prepareStatement(DELETE_PHOTOS_BY_ORDER);
             PreparedStatement statementOrders = connection.prepareStatement(DELETE_ORDER_BY_ID)) {
            connection.setAutoCommit(false);
            try {
                statementPhotos.setLong(1, id);
                statementPhotos.executeUpdate();
                statementOrders.setLong(1, id);
                statementOrders.executeUpdate();
                connection.commit();
                log.info("Order by id: {} and photos deleted from DB", id);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error during delete order", e);
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("Error during delete order {}", id, e);
            throw new RuntimeException("Error - Order is not deleted from db", e);
        }
    }

    @Override

    public int add(Order order) {
        log.info("Create new order");
        int orderId = 0;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            preparedStatement.setInt(2, orderStatusDao.getOrderStatusIdByStatusName(order.getStatus()));
            preparedStatement.setLong(3, order.getUser().getId());
            preparedStatement.setString(4, order.getComment());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                }
               log.info("Order {} created and added to DB", order);
            } catch (SQLException e) {
                log.error("Error during create order {}", order, e);
                throw new RuntimeException("Error during create order", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            log.info("Order {} created and added to DB", order);
        }
        return orderId;
    }


    @Override
    // private static final String SAVE_PHOTO_PATH =""
    public void savePhotos(int orderId, List<String> photosPaths) {
//        LOG.info("");
//        for (String pathToPhoto : photosPaths) {
//            try (Connection connection = dataSource.getConnection();
//                 PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PHOTO_PATH)) {
//                preparedStatement.setString(1, pathToPhoto);
//                preparedStatement.setInt(2, orderId);
//                preparedStatement.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    String getPartWhere(FilterParameters filterParameters) {
        StringJoiner stringJoiner = new StringJoiner(" AND ", " WHERE ", "");
        if (filterParameters.getEmail() != null) {
            stringJoiner.add("u.email=?");
        }
        if (filterParameters.getFromDate() != null) {
            stringJoiner.add("o.orderDate>=?");
        }
        if (filterParameters.getToDate() != null) {
            stringJoiner.add("o.orderDate<=?");
        }
        if (filterParameters.getOrderStatus() != null) {
            stringJoiner.add("os.statusName=?");
        }
        if (filterParameters.getPhoneNumber() != null) {
            stringJoiner.add("u.phoneNumber=?");
        }
        return stringJoiner.toString();
    }

    private String addSort(String query) {
        return query + " ORDER BY o.id DESC";
    }
}