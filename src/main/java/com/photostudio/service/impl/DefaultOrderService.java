package com.photostudio.service.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.dao.LocalDiskPhotoDao;
import com.photostudio.dao.OrderDao;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.Order;
import com.photostudio.service.OrderService;

import java.util.List;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ServiceLocator.getService(OrderDao.class);

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public List<Order> getOrdersByParameters(FilterParameters filterParameters) {
        return orderDao.getOrdersByParameters(filterParameters);
    }

    @Override
    public void delete(long id) {
        LocalDiskPhotoDao photoDao = ServiceLocator.getService(LocalDiskPhotoDao.class);
        photoDao.deleteByOrder(id);
        orderDao.delete(id);
    }


}
