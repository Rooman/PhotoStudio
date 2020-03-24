package com.photostudio;

import com.photostudio.dao.OrderStatusDao;
import com.photostudio.dao.PhotoDao;
import com.photostudio.dao.file.LocalDiskPhotoDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.dao.OrderDao;
import com.photostudio.dao.UserDao;
import com.photostudio.dao.jdbc.DataSourceFactory;
import com.photostudio.dao.jdbc.JdbcOrderDao;
import com.photostudio.dao.jdbc.JdbcOrderStatusCachedDao;
import com.photostudio.dao.jdbc.JdbcUserDao;
import com.photostudio.security.SecurityService;
import com.photostudio.security.impl.DefaultSecurityService;
import com.photostudio.service.MailService;
import com.photostudio.service.OrderService;
import com.photostudio.service.OrderStatusService;
import com.photostudio.service.UserService;
import com.photostudio.service.impl.DefaultMailService;
import com.photostudio.service.impl.DefaultOrderService;
import com.photostudio.service.impl.DefaultOrderStatusService;
import com.photostudio.service.impl.DefaultUserService;
import com.photostudio.util.PropertyReader;
import com.photostudio.web.util.MailSender;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    static {
        //config property reader util class
        PropertyReader propertyReader = new PropertyReader("application.properties");
        register(PropertyReader.class, propertyReader);

        //config db connection
        DataSource dataSource = new DataSourceFactory(propertyReader).createDataSource();

        UserDao userDao = new JdbcUserDao(dataSource);
        register(UserDao.class, userDao);

        OrderStatusDao orderStatusDao = new JdbcOrderStatusCachedDao(dataSource);
        register(OrderStatusDao.class, orderStatusDao);

        OrderStatusService orderStatusService = new DefaultOrderStatusService(orderStatusDao);
        register(OrderStatusService.class, orderStatusService);

        OrderDao orderDao = new JdbcOrderDao(dataSource);
        register(OrderDao.class, orderDao);

        PhotoDao photoDiskDao = new LocalDiskPhotoDao(propertyReader.getString("dir.photo"));
        register(PhotoDao.class, photoDiskDao);

        UserService userService = new DefaultUserService(userDao);
        register(UserService.class, userService);

        MailSender mailSender = new MailSender();
        register(MailSender.class, mailSender);

        MailService mailService = new DefaultMailService(mailSender, userService);
        register(MailService.class, mailService);

        OrderService orderService = new DefaultOrderService(orderDao, photoDiskDao, orderStatusService, mailService);
        register(OrderService.class, orderService);

        SecurityService securityService = new DefaultSecurityService();
        register(SecurityService.class, securityService);

        //mapper for JSON
        ObjectMapper mapper = new ObjectMapper();
        register(ObjectMapper.class, mapper);

    }

    public static <T> T getService(Class<T> serviceClass) {
        T service = serviceClass.cast(SERVICES.get(serviceClass));
        return service;
    }

    private static void register(Class<?> serviceClass, Object service) {
        SERVICES.put(serviceClass, service);
    }
}