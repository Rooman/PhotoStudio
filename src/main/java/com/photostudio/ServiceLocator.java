package com.photostudio;

import com.photostudio.dao.*;
import com.photostudio.dao.file.LocalDiskPhotoDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.dao.jdbc.*;
import com.photostudio.security.SecurityService;
import com.photostudio.security.impl.DefaultSecurityService;
import com.photostudio.service.*;
import com.photostudio.service.impl.*;
import com.photostudio.util.PropertyReader;
import com.photostudio.web.util.MailSender;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    static {
        //mapper for JSON
        ObjectMapper mapper = new ObjectMapper();
        register(ObjectMapper.class, mapper);

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

        MailSender mailSender = new MailSender(propertyReader);
        register(MailSender.class, mailSender);

        NotificationService notificationService = new WSNotificationService(mapper);
        register(NotificationService.class, notificationService);

        EmailTemplateDao emailTemplateDao = new JdbcEmailTemplateCachedDao(dataSource);
        MailService mailService = new DefaultMailService(mailSender, userService, emailTemplateDao, notificationService);
        register(MailService.class, mailService);

        OrderService orderService = new DefaultOrderService(orderDao, photoDiskDao, orderStatusService, mailService);
        register(OrderService.class, orderService);
        userService.setOrderService(orderService);

        SecurityService securityService = new DefaultSecurityService();
        register(SecurityService.class, securityService);

        UserLanguageDao userLanguageDao = new JdbcUserLanguageCachedDao(dataSource);
        register(UserLanguageDao.class, userLanguageDao);

        UserLanguageService userLanguageService = new DefaultUserLanguageService(userLanguageDao);
        register(UserLanguageService.class, userLanguageService);

    }

    public static <T> T getService(Class<T> serviceClass) {
        T service = serviceClass.cast(SERVICES.get(serviceClass));
        return service;
    }

    private static void register(Class<?> serviceClass, Object service) {
        SERVICES.put(serviceClass, service);
    }
}