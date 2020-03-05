package com.photostudio.service.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.PhotoDao;
import com.photostudio.dao.OrderDao;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultOrderService implements OrderService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderDao orderDao = ServiceLocator.getService(OrderDao.class);
    private PhotoDao photoDao = ServiceLocator.getService(PhotoDao.class);

    @Override
    public List<Order> getAll() {
        LOG.info("Started service get all orders from DB");
        return orderDao.getAll();
    }

    @Override
    public List<Order> getOrdersByParameters(FilterParameters filterParameters) {
        LOG.info("Started service get orders by parameters from DB");
        return orderDao.getOrdersByParameters(filterParameters);
    }

    @Override
    public Order getOrderByIdInStatusNew(int id) {
        LOG.info("Started service get order by id:{} in status NEW from DB", id);
        return orderDao.getOrderByIdInStatusNew(id);
    }


    @Override
    public List<Order> getOrdersByUserId(long userId) {
        LOG.info("Started service get orders by userId from DB");
        return orderDao.getOrdersByUserId(userId);
    }

    @Override
    public void delete(long id) {
        LOG.info("Started service delete order by id ");
        photoDao.deleteByOrder(id);
        orderDao.delete(id);
    }

    @Override
    public void moveStatusForward(long id, UserRole userRole) {
        LOG.info("Started service set next status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        if (checkByDBStatusForward(statusDb, userRole)) {
            orderDao.changeOrderStatus(id, true);
        } else{
            LOG.error("Order status " + statusDb.getOrderStatusName() + " can't be changed forward");
            throw new ChangeOrderStatusInvalidException("Order status " + statusDb.getOrderStatusName() + " can't be changed forward ");
        }
    }

    @Override
    public void moveStatusBack(long id, UserRole userRole) {
        LOG.info("Started service set previous status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        if (checkByDBStatusBack(statusDb, userRole)) {
            orderDao.changeOrderStatus(id, false);
        } else{
            LOG.error("Order status " + statusDb.getOrderStatusName() + " can't be changed back");
            throw new ChangeOrderStatusInvalidException("Order status " + statusDb.getOrderStatusName() + " can't be changed back");
        }
    }

    private boolean checkByDBStatusForward(OrderStatus statusDB, UserRole userRole) {
        LOG.info("Check status in DB for change status forward: {} by user {}", statusDB, userRole);
        boolean isCorrect = true;
        if ((statusDB == OrderStatus.NEW || statusDB == OrderStatus.SELECTED) && userRole == UserRole.USER) {
            isCorrect = false;
        }

        if (isCorrect && statusDB == OrderStatus.VIEW_AND_SELECT && userRole == UserRole.ADMIN) {
            isCorrect = false;
        }

        if (isCorrect && statusDB == OrderStatus.READY) {
            isCorrect = false;
        }

        return isCorrect;
    }

    private boolean checkByDBStatusBack(OrderStatus statusDB, UserRole userRole) {
        LOG.info("Check status in DB for change status back: {} by user ", statusDB, userRole);
        boolean isCorrect = true;
        if (statusDB != OrderStatus.READY) {
            isCorrect = false;
        }
        if (isCorrect && userRole == UserRole.ADMIN) {
            isCorrect = false;
        }
        return isCorrect;
    }

    //For tests
    void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
