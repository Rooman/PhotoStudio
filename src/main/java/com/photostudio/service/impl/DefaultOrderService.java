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
import com.photostudio.web.util.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultOrderService implements OrderService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderDao orderDao = ServiceLocator.getService(OrderDao.class);
    private PhotoDao photoDao = ServiceLocator.getService(PhotoDao.class);
    private MailSender mailSender = ServiceLocator.getService(MailSender.class);

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
    public void moveStatusForward(long id, User user) {
        LOG.info("Started service set next status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        if (checkByDBStatusForward(statusDb, user.getUserRole())) {
            orderDao.changeOrderStatus(id, true);
            sendMail(user.getEmail(), id,statusDb.ordinal() + 2);//+1 - next, +1 because enum from 0
        } else {
            LOG.error("Order status " + statusDb.getOrderStatusName() + " can't be changed forward");
            throw new ChangeOrderStatusInvalidException("Order status " + statusDb.getOrderStatusName() + " can't be changed forward ");
        }
    }

    @Override
    public void moveStatusBack(long id, User user) {
        LOG.info("Started service set previous status for order:{}", id);
        OrderStatus statusDb = orderDao.getOrderStatus(id);
        if (checkByDBStatusBack(statusDb, user.getUserRole())) {
            orderDao.changeOrderStatus(id, false);
            sendMail(user.getEmail(), id, statusDb.ordinal()); //-1
        } else {
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

    private void sendMail(String userMail, long orderId, int statusOrder) {
        LOG.info("Send mail after changong status :{}", statusOrder);
        switch (statusOrder) {
            case 2: {
                mailSender.send("Order " + orderId + " is created", "You can choose photo in order " + orderId, userMail);
                break;
            }
            case 3: {
                mailSender.send("User " + userMail + " selected photo for order:", "", userMail);
                break;
            }
            case 4: {
                mailSender.send("Order "+orderId +" ready", "You can download selected photo", userMail);
                break;
            }
        }
    }

    //For tests
    void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
