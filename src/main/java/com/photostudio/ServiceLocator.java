package com.photostudio;

import com.photostudio.dao.OrderDao;
import com.photostudio.dao.UserDao;
import com.photostudio.dao.jdbc.DataSourceFactory;
import com.photostudio.dao.jdbc.JdbcOrderDao;
import com.photostudio.dao.jdbc.JdbcUserDao;
import com.photostudio.service.OrderService;
import com.photostudio.service.UserService;
import com.photostudio.service.impl.DefaultOrderService;
import com.photostudio.service.impl.DefaultUserService;
import com.photostudio.util.PropertyReader;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    static {
        //config properties
        Properties properties = new PropertyReader("application.properties").getProperties();

        //config db connection
        DataSource dataSource = new DataSourceFactory(properties).createDataSource();

        UserDao userDao = new JdbcUserDao(dataSource);
        register(UserDao.class, userDao);

        OrderDao orderDao = new JdbcOrderDao(dataSource);
        register(OrderDao.class, orderDao);

        UserService productService = new DefaultUserService(userDao);
        register(UserService.class, productService);

        OrderService orderService = new DefaultOrderService();
        register(OrderService.class, orderService);

    }

    public static <T> T getService(Class<T> serviceClass) {
        T service = serviceClass.cast(SERVICES.get(serviceClass));
        return service;
    }

    private static void register(Class<?> serviceClass, Object service) {
        SERVICES.put(serviceClass, service);
    }
}
