package com.photostudio.dao.jdbc;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.OrderDao;
import com.photostudio.dao.PhotoDao;
import com.photostudio.dao.jdbc.mapper.OrderRowMapper;
import com.photostudio.dao.jdbc.mapper.PhotoSourceRowMapper;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.photo.Photo;
import com.photostudio.entity.photo.PhotoStatus;
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
            "o.commentAdmin commentAdmin, " +
            "o.commentUser commentUser " +
            "FROM Orders o " +
            "JOIN OrderStatus os ON o.statusId = os.id " +
            "JOIN Users u ON o.userId = u.id";

    private static final String GET_ORDER_BY_ID = "SELECT o.id, statusName, orderDate, email, phoneNumber, commentAdmin, commentUser " +
            "FROM Orders o " +
            "JOIN OrderStatus os ON o.statusId = os.id " +
            "JOIN Users u ON o.userId = u.id " +
            "WHERE o.id=?;";

    private static final String GET_PHOTOS_BY_ORDER_ID = "SELECT id, source, photoStatusId FROM OrderPhotos WHERE orderId=?;";
    private static final String GET_ORDER_STATUS = "SELECT os.statusName FROM Orders o JOIN OrderStatus os ON o.statusId = os.id WHERE o.id = ?";
    private static final String DELETE_PHOTO_BY_ID = "DELETE FROM OrderPhotos WHERE id = ?";
    private static final String DELETE_PHOTOS_BY_ORDER = "DELETE FROM OrderPhotos WHERE orderId = ?";
    private static final String DELETE_ORDER_BY_ID = "DELETE FROM Orders WHERE id = ?";
    private static final String DELETE_PHOTOS_BY_ORDERS_ID = "DELETE FROM OrderPhotos WHERE orderId IN ";
    private static final String DELETE_ORDERS_BY_USER_ID = "DELETE FROM Orders WHERE userId = ?";
    private static final String UPDATE_STATUS = "UPDATE Orders o SET o.statusId = ?  WHERE o.id = ?";
    private static final String UPDATE_ORDER_BY_ADMIN = "UPDATE Orders o SET o.userId = ?, o.commentAdmin = ?  WHERE o.id = ?";
    private static final String UPDATE_ORDER_BY_USER = "UPDATE Orders o SET o.commentUser = ?  WHERE o.id = ?";
    private static final String ADD_NEW_ORDER = "INSERT INTO Orders (orderDate, statusId, userId, commentAdmin) VALUES (?, " +
            "?, ?, ?)";
    private static final String SAVE_PHOTO_PATH = "INSERT INTO OrderPhotos  (source, photoStatusId, orderId) VALUES(?,?,?);";
    private static final String GET_COUNT_PHOTO = "SELECT COUNT(*) FROM OrderPhotos WHERE orderId = ?";
    private static final String GET_COUNT_PHOTO_BY_STATUS = "SELECT COUNT(*) FROM OrderPhotos WHERE orderId = ? AND photoStatusId = ?";
    private static final String GET_PATH_PHOTO_BY_ID = "SELECT source FROM OrderPhotos WHERE id = ?";
    private static final String UPDATE_ALL_PHOTOS_SELECTED = "UPDATE OrderPhotos SET photoStatusId = 2 WHERE photoStatusId = 1 AND orderId = ? ";
    private static final String UPDATE_LIST_PHOTOS_SELECTED = "UPDATE OrderPhotos SET photoStatusId = 2 WHERE photoStatusId = 1 AND orderId = ? AND id IN (%s)";
    //private static final String UPDATE_PAID_PHOTOS = "UPDATE OrderPhotos SET photoStatusId = 3 WHERE orderId = ? AND photoStatusId = 2";

    private static final String GET_PHOTOS_BY_STATUS_AND_ORDER_ID = "SELECT * FROM OrderPhotos WHERE orderId=? AND photoStatusId = ?";
    private static final String GET_PHOTOS_SOURCES_BY_ORDER_ID = "SELECT * FROM OrderPhotos WHERE orderId=?";
    private static final String UPDATE_RETOUCHED_PHOTOS_STATUS = "UPDATE OrderPhotos SET photoStatusId = 3 WHERE source = ? AND orderId = ? AND photoStatusId=2";

    private static final OrderRowMapper ORDER_ROW_MAPPER = new OrderRowMapper();
    private static final PhotoSourceRowMapper PHOTO_SOURCE_ROW_MAPPER = new PhotoSourceRowMapper();

    private DataSource dataSource;

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
        String sql = GET_ALL_ORDERS + " WHERE o.userId = ?";
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
    public Order getOrderById(int id) {
        log.info("Started service get order by id: {} in status NEW from DB", id);
        String resultQuery = GET_PHOTOS_BY_ORDER_ID + GET_ORDER_BY_ID;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resultQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);

            preparedStatement.execute();
            List<Photo> photoSources = new ArrayList<>();

            try (ResultSet photoResultSet = preparedStatement.getResultSet()) {
                log.info("Assemble photo sources for order with id: {}", id);
                while (photoResultSet.next()) {
                    Photo photoSource = PHOTO_SOURCE_ROW_MAPPER.mapRow(photoResultSet);
                    photoSources.add(photoSource);
                }
            }
            if (!preparedStatement.getMoreResults()) {
                log.error("Get more resultSet for order with id: {} error", id);
                throw new RuntimeException("Get more resultSet for order with id: " + id + " error");
            }
            try (ResultSet orderResultSet = preparedStatement.getResultSet()) {
                log.info("Assemble main information about order with id: {}", id);
                orderResultSet.next();
                Order order = ORDER_ROW_MAPPER.mapRow(orderResultSet);
                order.getPhotoSources().addAll(photoSources);

                return order;
            }

        } catch (SQLException e) {
            log.error("Get order by id: {} in status NEW error", id, e);
            throw new RuntimeException("Get order by id " + id + " in status NEW error", e);
        }

    }

    @Override
    public void deletePhoto(long photoId) {
        log.info("Delete photo by id: {} ", photoId);
        try (Connection connection = dataSource.getConnection()) {
            Executor.execUpdate(connection, DELETE_PHOTO_BY_ID, photoId);
        } catch (SQLException e) {
            log.error("Error during delete photo {}", photoId, e);
            throw new RuntimeException("Error during delete photo " + photoId, e);
        }
    }

    @Override
    public void deletePhotos(int orderId) {
        log.info("Delete photos from order {}", orderId);
        try (Connection connection = dataSource.getConnection()) {
            Executor.execUpdate(connection, DELETE_PHOTOS_BY_ORDER, orderId);
        } catch (SQLException e) {
            log.error("Error during delete photo from order{}", orderId, e);
            throw new RuntimeException("Error during delete photo from order " + orderId, e);
        }
    }

    @Override
    public void delete(int id) {
        log.info("Delete order by id: {}", id);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Executor.execUpdate(connection, DELETE_PHOTOS_BY_ORDER, id);
                Executor.execUpdate(connection, DELETE_ORDER_BY_ID, id);
                connection.commit();
                log.info("Order by id: {} and photos deleted from DB", id);
            } catch (SQLException e) {
                connection.rollback();
                log.error("Rollback- Error during delete order {}", id, e);
                throw new RuntimeException("Error during delete order", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("Error during delete order {}", id, e);
            throw new RuntimeException("Error - Order is not deleted from db", e);
        }
    }

    @Override
    public void deleteOrdersByUserId(List<Order> orderList, long id) {
        log.info("Delete orders by user id: {}", id);
        String deletePhotosStatement = getDeletePhotoStatement(orderList);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statementPhotos = connection.prepareStatement(deletePhotosStatement);
             PreparedStatement statementOrders = connection.prepareStatement(DELETE_ORDERS_BY_USER_ID)) {
            connection.setAutoCommit(false);
            try {
                int count = 1;
                for (Order order : orderList) {
                    statementPhotos.setLong(count++, order.getId());
                }
                statementPhotos.executeUpdate();
                statementOrders.setLong(1, id);
                statementOrders.executeUpdate();
                connection.commit();
                log.info("Order by user id: {} and photos deleted from DB", id);
            } catch (SQLException e) {
                connection.rollback();
                log.error("Rollback- Error during delete orders by user id: {}", id, e);
                throw new RuntimeException("Error during delete order by user id " + id, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("Error during delete order by user id: {}", id, e);
            throw new RuntimeException("Error - Orders are not deleted from db", e);
        }
    }

    @Override
    public void changeOrderStatus(int id, int orderStatusId) {
        log.info("Change order status by id: {} new status : {}", id, orderStatusId);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS)) {

            statement.setInt(1, orderStatusId);
            statement.setLong(2, id);

            statement.execute();

            log.info("Order status changed successfully");
        } catch (SQLException e) {
            log.error("Error during changing status order id= {}", id, e);
            throw new RuntimeException("Error - Order status is not changed", e);
        }
    }

    @Override
    public OrderStatus getOrderStatus(int id) {
        log.info("Get order status by id: {}", id);
        OrderStatus status = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ORDER_STATUS)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    status = OrderStatus.getOrderStatus(resultSet.getString(1));
                    log.info("Order status: {}", status);
                } else {
                    log.error("Order {} is not found in DB", id);
                    throw new RuntimeException("Order " + id + " is not found in DB");
                }
            }
        } catch (SQLException e) {
            log.error("Error during get status order id = {}", id, e);
            throw new RuntimeException("Error during get status order id = " + id, e);
        }
        return status;
    }

    @Override
    public int getPhotoCount(int id) {
        log.info("Get photo count for order id: {}", id);
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_COUNT_PHOTO)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                    log.info("Count photos: {}", result);
                }
            }
            return result;
        } catch (SQLException e) {
            log.error("Error during execution query GET_COUNT_PHOTO for order id = {}", id, e);
            throw new RuntimeException("Error during execution query GET_COUNT_PHOTO for order id = " + id, e);
        }
    }

    @Override
    public int getPhotoCountByStatus(int id, int idPhotoStatus) {
        log.info("Get photo count for order id: {}, photo status: {}", id, idPhotoStatus);
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_COUNT_PHOTO_BY_STATUS)) {
            statement.setLong(1, id);
            statement.setInt(2, idPhotoStatus);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                    log.info("Count photos: {}", result);
                }
            }
            return result;
        } catch (SQLException e) {
            log.error("Error during execution query GET_COUNT_PHOTO_BY_STATUS for order id = {}", id, e);
            throw new RuntimeException("Error during execution query GET_COUNT_PHOTO_BY_STATUS for order id = " + id, e);
        }
    }

    @Override
    public int add(Order order, int orderStatusId) {
        log.info("Create new order");
        int orderId = 0;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            preparedStatement.setInt(2, orderStatusId);
            preparedStatement.setLong(3, order.getUser().getId());
            preparedStatement.setString(4, order.getCommentAdmin());
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
    public void editOrderByAdmin(int orderId, long userId, String commentAdmin) {
        log.info("Edit order by admin in DB");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_BY_ADMIN)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, commentAdmin);
            preparedStatement.setInt(3, orderId);

            preparedStatement.execute();

            log.info("Admin info was updated in DB for order {}", orderId);
        } catch (SQLException e) {
            log.error("Error during edit order info by Admin in DB {}", orderId, e);
            throw new RuntimeException("Error during edit order info by Admin in DB", e);
        }
    }

    @Override
    public void editOrderByUser(int orderId, String commentUser) {
        log.info("Edit order by user in DB: commentUser = {}", commentUser);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_BY_USER);
        ) {

            preparedStatement.setString(1, commentUser);
            preparedStatement.setInt(2, orderId);

            preparedStatement.execute();

            log.info("User comment was updated in DB for order {}", orderId);
        } catch (SQLException e) {
            log.error("Error during edit order info by User in DB {}", orderId, e);
            throw new RuntimeException("Error during edit order info by User in DB", e);
        }
    }

    @Override
    public List<String> getPhotosSourcesByOrderId(int orderId){
        log.info("Get photos sources by orderId : {}", orderId);
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_PHOTOS_SOURCES_BY_ORDER_ID)){
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> photosSources = new ArrayList<>();
            while (resultSet.next()) {
                Photo photo  = PHOTO_SOURCE_ROW_MAPPER.mapRow(resultSet);
                photosSources.add(photo.getSource());
            }
            log.info("Get: {} photosSources from DB", photosSources.size());
            log.debug("Get all photosSources: {}", photosSources);
            return photosSources;

        }catch (SQLException e){
            log.error("Error during get photo with orderId : {}", orderId);
            throw new RuntimeException("Error during get photo with orderId "+ orderId, e);
        }
    }

    @Override
    public void savePhotos(int orderId, List<String> photosPaths) {
        log.info("Save photos to DB");
        for (String pathToPhoto : photosPaths) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PHOTO_PATH)) {
                preparedStatement.setString(1, pathToPhoto);
                preparedStatement.setInt(2, 1);
                preparedStatement.setInt(3, orderId);
                preparedStatement.executeUpdate();
                log.info("Photos added to DB");
            } catch (SQLException e) {
                log.error("Error during save photo to DB with orderId : {}", orderId, e);
                throw new RuntimeException("Error during save photo to DB", e);
            }
        }
    }

    @Override
    public void updateStatusRetouchedPhotos(List<String> photosPaths, int orderId) {
        log.info("Update retouched photos status");
        for (String pathToPhoto : photosPaths) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RETOUCHED_PHOTOS_STATUS)) {
                preparedStatement.setString(1, pathToPhoto);
                preparedStatement.setInt(2, orderId);
                preparedStatement.executeUpdate();
                log.info("Photo :{} status is retouched", pathToPhoto);
            } catch (SQLException e) {
                log.error("Error during change status photo : {}", pathToPhoto);
                throw new RuntimeException("Error during change status photo "+pathToPhoto, e);
            }
        }
    }

    @Override
    public void selectPhotos(int orderId, String selectedPhotos) {
        log.info("Select photos in DB, order : {}, photos : {}", orderId, selectedPhotos);
        String sql = (selectedPhotos.equals("all")) ? UPDATE_ALL_PHOTOS_SELECTED : String.format(UPDATE_LIST_PHOTOS_SELECTED, selectedPhotos);
        try (Connection connection = dataSource.getConnection()) {
            Executor.execute(connection, sql, orderId);
            log.info("Photos with {} ids selected", selectedPhotos);
        } catch (SQLException e) {
            log.error("Error during execution update photos to selected", e);
            throw new RuntimeException("Error during execution update photos to selected", e);
        }
    }

    public List<Photo> getPhotosByStatus(int orderId, PhotoStatus photoStatus) {
        log.info("Get photos with status : {} from DB", photoStatus.getName());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PHOTOS_BY_STATUS_AND_ORDER_ID)) {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, photoStatus.getId());
            preparedStatement.execute();
            List<Photo> photos = new ArrayList<>();
            try (ResultSet photoResultSet = preparedStatement.getResultSet()) {
                log.info("Assemble photo sources for order with id: {}", orderId);
                while (photoResultSet.next()) {
                    Photo photo = PHOTO_SOURCE_ROW_MAPPER.mapRow(photoResultSet);
                    photos.add(photo);
                }
            }
            return photos;
        } catch (SQLException e) {
            log.error("Get paid photos by orderId: {} error", orderId, e);
            throw new RuntimeException("Get paid photos by orderId" + orderId + "error", e);
        }
    }

    public String getPathByPhotoId(long photoId) {
        log.info("Get path by photo id: {}", photoId);
        String result = "";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PATH_PHOTO_BY_ID)) {
            statement.setLong(1, photoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString(1);
                    log.info("PhotoPath: {}", result);
                }
            }
            return result;
        } catch (SQLException e) {
            log.error("Error during execution query get photo path by id = {}", photoId, e);
            throw new RuntimeException("Error during execution query get photo path by id = " + photoId, e);
        }
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
        return query + " ORDER BY o.statusId, o.orderDate DESC";
    }

    String getDeletePhotoStatement(List<Order> orderList) {
        StringJoiner stringJoiner = new StringJoiner(", ", DELETE_PHOTOS_BY_ORDERS_ID + "(", ")");
        orderList.forEach(order -> stringJoiner.add("?"));
        return stringJoiner.toString();
    }
}