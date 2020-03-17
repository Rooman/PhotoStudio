package com.photostudio.service.impl;

import com.photostudio.dao.PhotoDao;
import com.photostudio.dao.OrderDao;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;

import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
import com.photostudio.service.MailService;

import com.photostudio.service.OrderCacheService;
import com.photostudio.service.OrderService;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.Part;
import java.util.List;

@Slf4j
public class DefaultOrderService implements OrderService {
    private OrderDao orderDao;
    private PhotoDao photoDao;
    private OrderCacheService orderCacheService;
    private MailService mailService;

    public DefaultOrderService(OrderDao orderDao, PhotoDao photoDao, OrderCacheService orderCacheService, MailService mailService) {
        this.orderDao = orderDao;
        this.photoDao = photoDao;
        this.orderCacheService = orderCacheService;
        this.mailService = mailService;
    }

    @Override
    public List<Order> getAll() {
        log.info("Started service get all orders from DB");
        return orderDao.getAll();
    }

    @Override
    public List<Order> getOrdersByParameters(FilterParameters filterParameters) {
        log.info("Started service get orders by parameters from DB");
        return orderDao.getOrdersByParameters(filterParameters);
    }

    @Override
    public Order getOrderByIdInStatusNew(int id) {
        log.info("Started service get order by id:{} in status NEW from DB", id);
        return orderDao.getOrderByIdInStatusNew(id);
    }

    @Override
    public List<Order> getOrdersByUserId(long userId) {
        log.info("Started service get orders by userId from DB");
        return orderDao.getOrdersByUserId(userId);
    }

    public int add(Order order, List<Part> photoToUpload) {
        log.info("Started creating new order {}", order);
        int orderId = orderDao.add(order, orderCacheService.getOrderStatusIdByStatusName(order.getStatus()));
        List<String> photosPath = photoDao.savePhotoByOrder(photoToUpload, orderId);
        orderDao.savePhotos(order, orderId, photosPath);
        return orderId;
    }

    @Override
    public void delete(long id) {
        log.info("Started service delete order by id ");
        photoDao.deleteByOrder(id);
        orderDao.delete(id);
    }

    @Override

    public void moveStatusForward(long id, User user) {
        log.info("Started service set next status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        if (checkByDBStatusForward(statusDb, user.getUserRole())) {
            orderDao.changeOrderStatus(id, true);
            mailService.sendOnChangeStatus(user, id, statusDb.ordinal() + 2);//+1 - next, +1 because enum from 0
        } else {
            log.error("Order status " + statusDb.getOrderStatusName() + " can't be changed forward");
            throw new ChangeOrderStatusInvalidException("Order status " + statusDb.getOrderStatusName() + " can't be changed forward ");
        }
    }

    @Override
    public void moveStatusBack(long id, User user) {
        log.info("Started service set previous status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        if (checkByDBStatusBack(statusDb, user.getUserRole())) {
            orderDao.changeOrderStatus(id, false);
            mailService.sendOnChangeStatus(user, id, statusDb.ordinal()); //-1
        } else {
            log.error("Order status " + statusDb.getOrderStatusName() + " can't be changed back");
            throw new ChangeOrderStatusInvalidException("Order status " + statusDb.getOrderStatusName() + " can't be changed back");
        }
    }

    private boolean checkByDBStatusForward(OrderStatus statusDB, UserRole userRole) {
        log.info("Check status in DB for change status forward: {} by user {}", statusDB, userRole);
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
        log.info("Check status in DB for change status back: {} by user ", statusDB, userRole);
        boolean isCorrect = true;
        if (statusDB != OrderStatus.READY) {
            isCorrect = false;
        }
        if (isCorrect && userRole == UserRole.ADMIN) {
            isCorrect = false;
        }
        return isCorrect;
    }


}
