package com.photostudio;

import com.photostudio.dao.OrderDao;
import com.photostudio.dao.UserDao;
import com.photostudio.dao.jdbc.DataSourceFactory;
import com.photostudio.dao.jdbc.JdbcOrderDao;
import com.photostudio.dao.jdbc.JdbcUserDao;
import com.photostudio.security.SecurityService;
import com.photostudio.security.impl.DefaultSecurityService;
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

        OrderService orderService = new DefaultOrderService();
        register(OrderService.class, orderService);

        SecurityService securityService = new DefaultSecurityService();
        register(SecurityService.class, securityService);

        UserService userService = new DefaultUserService(userDao, securityService);
        register(UserService.class, userService);
    }

    public static <T> T getService(Class<T> serviceClass) {
        T service = serviceClass.cast(SERVICES.get(serviceClass));
        return service;
    }

    private static void register(Class<?> serviceClass, Object service) {
        SERVICES.put(serviceClass, service);
    }
}
