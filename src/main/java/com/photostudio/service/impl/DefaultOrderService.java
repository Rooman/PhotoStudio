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
import com.photostudio.exception.entity.ErrorChangeOrderStatus;
import com.photostudio.service.MailService;


import com.photostudio.service.OrderService;
import com.photostudio.service.OrderStatusService;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.Part;
import java.util.List;
import java.util.stream.Collectors;


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

    DefaultOrderService(OrderDao orderDao, OrderStatusService orderStatusService, MailService mailService) {
        this.orderDao = orderDao;
        this.orderStatusService = orderStatusService;
        this.mailService = mailService;
    }

    DefaultOrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
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
        log.info("Started service delete order by id {}", id);
        photoDao.deleteByOrder(id);
        orderDao.delete(id);
    }

    @Override
    public void deletePhoto(int orderId, long photoId) {
        log.info("Started service delete photo by id {}", photoId);
        String photoSource = orderDao.getPathByPhotoId(photoId);
        orderDao.deletePhoto(photoId);
        photoDao.deletePhoto(orderId, photoSource);
    }

    @Override
    public void deletePhotos(int orderId) {
        log.info("Started service delete all photos from order {}", orderId);
        orderDao.deletePhotos(orderId);
        photoDao.deleteByOrder(orderId);
    }

    @Override
    public void moveStatusForward(int id, User user) {
        log.info("Started service set next status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        OrderStatus newStatus = statusDb.next();
        if (newStatus == null) {
            throw new ChangeOrderStatusInvalidException(ErrorChangeOrderStatus.INCORRECT_STATUS_FORWARD, statusDb);
        }
        changeStatus(id, user, newStatus);
    }

    @Override
    public void moveStatusBack(int id, User user) {
        log.info("Started service set previous status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        OrderStatus newStatus = statusDb.previous();
        if (newStatus == null) {
            throw new ChangeOrderStatusInvalidException(ErrorChangeOrderStatus.INCORRECT_STATUS_BACK, statusDb);
        }
        changeStatus(id, user, newStatus);
    }

    @Override
    public String getPathByPhotoId(long photoId) {
        log.info("Started service get path to photo by id:{}", photoId);
        return orderDao.getPathByPhotoId(photoId);
    }

    @Override
    public List<Order> getOrdersWithOrderStatusNotNewByUserId(long userId) {
        return getOrdersByUserId(userId).stream()
                .filter(order -> order.getStatus() != OrderStatus.NEW)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllOrdersByUserId(long id) {
        List<Order> orderList = getOrdersByUserId(id);
        if (!orderList.isEmpty()) {
            orderDao.deleteOrdersByUserId(orderList, id);
            orderList.stream().map(Order::getId).forEach(orderId -> photoDao.deleteByOrder(orderId));
        }
    }

    @Override
    public String getPathToOrderDir(int orderId) {
        return photoDao.getPathToOrderDir(orderId);
    }

    private void changeStatus(int orderId, User userChanged, OrderStatus newStatus) {
        if (checkUserRole(userChanged.getUserRole(), newStatus) && checkPhoto(orderId, newStatus)) {
            orderDao.changeOrderStatus(orderId, orderStatusService.getOrderStatusIdByStatusName(newStatus));
            mailService.sendOnChangeStatus(userChanged, orderId, newStatus);
        }
    }

    boolean checkUserRole(UserRole userRole, OrderStatus newStatus) {
        log.info("Check user role to change status to {} by user {}", newStatus, userRole);
        if ((newStatus == OrderStatus.VIEW_AND_SELECT || newStatus == OrderStatus.READY) && userRole == UserRole.USER) {
            throw new ChangeOrderStatusInvalidException(ErrorChangeOrderStatus.INCORRECT_STATUS_FOR_USER, newStatus);
        }

        if (newStatus == OrderStatus.SELECTED && userRole == UserRole.ADMIN) {
            throw new ChangeOrderStatusInvalidException(ErrorChangeOrderStatus.INCORRECT_STATUS_FOR_ADMIN, newStatus);
        }

        return true;
    }

    boolean checkPhoto(int orderId, OrderStatus newOrderStatus) {
        log.info("Check photo in DB for order: {} new status: {}", orderId, newOrderStatus);
        if (newOrderStatus == OrderStatus.VIEW_AND_SELECT) {
            if (orderDao.getPhotoCount(orderId) == 0) {
                throw new ChangeOrderStatusInvalidException(ErrorChangeOrderStatus.PHOTOS_SHOULD_BE_LOADED);
            }
        }
        if (newOrderStatus == OrderStatus.SELECTED) {
            if (orderDao.getPhotoCountByStatus(orderId, PhotoStatus.SELECTED.getId()) == 0) {
                throw new ChangeOrderStatusInvalidException(ErrorChangeOrderStatus.PHOTOS_SHOULD_BE_SELECTED);
            }
        }
        if (newOrderStatus == OrderStatus.READY) {
            if (orderDao.getPhotoCountByStatus(orderId, PhotoStatus.PAID.getId()) == 0) {
                throw new ChangeOrderStatusInvalidException(ErrorChangeOrderStatus.PHOTOS_SHOULD_BE_PAID);
            }
        }

        return true;
    }
}
