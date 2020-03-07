package com.photostudio.service.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.PhotoDao;
import com.photostudio.dao.file.LocalDiskPhotoDao;
import com.photostudio.dao.OrderDao;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.service.OrderCacheService;
import com.photostudio.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.util.List;

@Slf4j
public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ServiceLocator.getService(OrderDao.class);
    private PhotoDao photoDao = ServiceLocator.getService(PhotoDao.class);
    private OrderCacheService orderCacheService = ServiceLocator.getService(OrderCacheService.class);

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

    public void delete(long id) {
        log.info("Started service delete order by id ");
        photoDao.deleteByOrder(id);
        orderDao.delete(id);
    }

    @Override
    public int add(Order order,List<Part> photoToUpload) {
        log.info("Started creating new order {}", order);
        int orderId = orderDao.add(order, orderCacheService.getOrderStatusIdByStatusName(order.getStatus()));
        List<String> photosPath=photoDao.savePhotoByOrder(photoToUpload);
        orderDao.savePhotos(order,orderId,photosPath);
        return orderId;
    }


}
