package com.photostudio.service.impl;

import com.photostudio.dao.PhotoDao;
import com.photostudio.dao.OrderDao;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;

import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.photo.PhotoStatus;
import com.photostudio.entity.user.User;
import com.photostudio.entity.user.UserRole;
import com.photostudio.exception.ChangeOrderStatusInvalidException;
import com.photostudio.exception.entity.ChangeOrderStatusError;
import com.photostudio.service.MailService;

import com.photostudio.service.OrderService;
import com.photostudio.service.OrderStatusService;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.Part;
import java.util.List;


@Slf4j
public class DefaultOrderService implements OrderService {
    private OrderDao orderDao;
    private PhotoDao photoDao;
    private OrderStatusService orderStatusService;
    private MailService mailService;

    public DefaultOrderService(OrderDao orderDao, PhotoDao photoDao, OrderStatusService orderStatusService, MailService mailService) {
        this.orderDao = orderDao;
        this.photoDao = photoDao;
        this.orderStatusService = orderStatusService;
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
        int orderId = orderDao.add(order, orderStatusService.getOrderStatusIdByStatusName(order.getStatus()));
        List<String> photosPath = photoDao.savePhotoByOrder(photoToUpload, orderId);
        orderDao.savePhotos(order, orderId, photosPath);
        return orderId;
    }

    @Override
    public void delete(int id) {
        log.info("Started service delete order by id ");
        photoDao.deleteByOrder(id);
        orderDao.delete(id);
    }

    @Override
    public void moveStatusForward(int id, User user) {
        log.info("Started service set next status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        OrderStatus newStatus = orderStatusService.getNext(statusDb);
        if (checkByDBStatusForward(statusDb, user.getUserRole()) && checkPhoto(id, newStatus)) {
            orderDao.changeOrderStatus(id, orderStatusService.getOrderStatusIdByStatusName(newStatus));
            mailService.sendOnChangeStatus(user, id, newStatus);
        }
    }

    @Override
    public void moveStatusBack(int id, User user) {
        log.info("Started service set previous status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        OrderStatus newStatus = orderStatusService.getPrevious(statusDb);
        if (checkByDBStatusBack(statusDb, user.getUserRole()) && checkPhoto(id, newStatus)) {
            orderDao.changeOrderStatus(id, orderStatusService.getOrderStatusIdByStatusName(newStatus));
            mailService.sendOnChangeStatus(user, id, newStatus);
        }
    }

    private boolean checkByDBStatusForward(OrderStatus statusDB, UserRole userRole) {
        log.info("Check status in DB for change status forward: {} by user {}", statusDB, userRole);
        if ((statusDB == OrderStatus.NEW || statusDB == OrderStatus.SELECTED) && userRole == UserRole.USER) {
            throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.INCORRECT_STATUS_FOR_USER, statusDB);
        }

        if (statusDB == OrderStatus.VIEW_AND_SELECT && userRole == UserRole.ADMIN) {
            throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.INCORRECT_STATUS_FOR_ADMIN, statusDB);
        }

        if (statusDB == OrderStatus.READY) {
            throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.INCORRECT_STATUS_FORWARD, statusDB);
        }

        return true;
    }

    private boolean checkByDBStatusBack(OrderStatus statusDB, UserRole userRole) {
        log.info("Check status in DB for change status back: {} by user {}", statusDB, userRole);
        if (statusDB != OrderStatus.READY) {
            throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.INCORRECT_STATUS_BACK, statusDB);
        }
        if (userRole == UserRole.ADMIN) {
            throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.INCORRECT_STATUS_FOR_ADMIN, statusDB);
        }
        return true;
    }

    private boolean checkPhoto(int orderId, OrderStatus newOrderStatus) {
        log.info("Check photo in DB for order: {} new status: {}", orderId, newOrderStatus);
        boolean result = true;
        if (newOrderStatus == OrderStatus.VIEW_AND_SELECT) {
            if (orderDao.getPhotoCount(orderId) == 0) {
                throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.PHOTOS_SHOULD_BE_LOADED);
            }
        }
        if (newOrderStatus == OrderStatus.SELECTED) {
            if (orderDao.getPhotoCountByStatus(orderId, PhotoStatus.SELECTED.getId()) == 0) {
                throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.PHOTOS_SHOULD_BE_SELECTED);
            }
        }
        if (newOrderStatus == OrderStatus.READY) {
            if (orderDao.getPhotoCountByStatus(orderId, PhotoStatus.PAID.getId()) == 0) {
                throw new ChangeOrderStatusInvalidException(ChangeOrderStatusError.PHOTOS_SHOULD_BE_PAID);
            }
        }

        return result;
    }
}
