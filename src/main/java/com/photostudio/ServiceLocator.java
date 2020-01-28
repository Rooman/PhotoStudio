package com.photostudio;

import com.photostudio.dao.jdbc.ConnectionFactory;
import com.photostudio.dao.jdbc.JdbcUserDao;
import com.photostudio.dao.UserDao;
import com.photostudio.service.UserService;
import com.photostudio.service.impl.DefaultUserService;
import com.photostudio.util.PropertyReader;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();
    private static Properties properties;

    static {
        //config properties
        properties = new PropertyReader("application.properties").getProperties();

        //config db connection
        DataSource dataSource = new ConnectionFactory(properties);

        UserDao userDao = new JdbcUserDao(dataSource);
        register(UserDao.class, userDao);

        UserService productService = new DefaultUserService(userDao);
        register(UserService.class, productService);

    }

    public static <T> T getService(Class<T> serviceClass) {
        T service = serviceClass.cast(SERVICES.get(serviceClass));
        return service;
    }

    private static void register(Class<?> serviceClass, Object service) {
        SERVICES.put(serviceClass, service);
    }
}
