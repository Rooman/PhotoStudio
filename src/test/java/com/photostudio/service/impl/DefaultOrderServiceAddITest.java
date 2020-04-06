package com.photostudio.service.impl;


import com.photostudio.dao.EmailTemplateDao;
import com.photostudio.dao.TransactionManager;
import com.photostudio.dao.UserDao;
import com.photostudio.dao.entity.PhotoFile;
import com.photostudio.dao.file.LocalDiskPhotoDao;
import com.photostudio.dao.jdbc.*;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import com.photostudio.entity.order.Order;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.MailService;
import com.photostudio.service.OrderService;
import com.photostudio.service.OrderStatusService;
import com.photostudio.service.UserService;
import com.photostudio.service.testUtils.MockMailSender;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultOrderServiceAddITest {

    private static TestDataSource dataSource;
    private static DefaultOrderService orderService;
    private static final String TEST_PATH_PHOTO = "test_delete_orders";
    private static JdbcOrderDao jdbcOrderDao;
    private static OrderStatusService orderStatusService;
    private static TransactionManager transactionManager;

    @BeforeAll
    public static void before() throws SQLException, IOException {
        dataSource = new TestDataSource();
        JdbcDataSource jdbcDataSource = dataSource.init();
        jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);

        JdbcOrderStatusCachedDao jdbcOrderStatusCachedDao = new JdbcOrderStatusCachedDao(jdbcDataSource);
        orderStatusService = new DefaultOrderStatusService(jdbcOrderStatusCachedDao);

        //create dirs and files
        File dir = new File(TEST_PATH_PHOTO);
        dir.mkdirs();
        LocalDiskPhotoDao photoDao = new LocalDiskPhotoDao(TEST_PATH_PHOTO);


        orderService = new DefaultOrderService(jdbcOrderDao, orderStatusService, photoDao);

        transactionManager = new JdbcTransactionManager(jdbcDataSource);
        orderService.setTransactionManager(transactionManager);

        dataSource.runScript("db/data_change_status.sql");
    }

    @Test
    public void testAddAllFine() throws IOException, SQLException {

        int cntOrdersBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        List<PhotoFile> fileList = getTestListFiles();
        Order order = getNewOrderTest();

        int orderId = orderService.add(order, fileList);

        int cntOrdersAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        assertEquals(cntOrdersBefore + 1, cntOrdersAfter);
        assertEquals(cntPhotosBefore + fileList.size(), cntPhotosAfter);

        File dir = new File(TEST_PATH_PHOTO, "Order-" + orderId);
        assertTrue(dir.exists());
        assertEquals(2, dir.listFiles().length);

        File file = new File(dir, "firstFile.txt");
        assertTrue(file.exists());

        file = new File(dir, "secondFile.txt");
        assertTrue(file.exists());
    }

    @Test
    public void testAddOrderCreationError() throws SQLException, IOException {
        int cntOrdersBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        List<PhotoFile> fileList = getTestListFiles();
        Order order = getNewOrderTest();
        order.getUser().setId(20);
        assertThrows(Exception.class, () -> {
            orderService.add(order, fileList);
        });

        int cntOrdersAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        assertEquals(cntOrdersBefore, cntOrdersAfter);
        assertEquals(cntPhotosBefore, cntPhotosAfter);

        File dir = new File(TEST_PATH_PHOTO);
        assertEquals(0, dir.list().length);
    }

    @Test
    public void testAddOrderSaveToDiskError() throws SQLException, IOException {
        int cntOrdersBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        LocalDiskPhotoDao photoDao = new LocalDiskPhotoDao("X:/test_orders");

        DefaultOrderService orderServiceW = new DefaultOrderService(jdbcOrderDao, orderStatusService, photoDao);
        orderServiceW.setTransactionManager(transactionManager);

        List<PhotoFile> fileList = getTestListFiles();
        Order order = getNewOrderTest();

        assertThrows(Exception.class, () -> {
            orderServiceW.add(order, fileList);
        });

        int cntOrdersAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        assertEquals(cntOrdersBefore, cntOrdersAfter);
        assertEquals(cntPhotosBefore, cntPhotosAfter);

        File dir = new File(TEST_PATH_PHOTO);
        assertEquals(0, dir.list().length);
    }

    @Test
    public void testAddOrderAddSourcesError() throws SQLException, IOException {
        int cntOrdersBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosBefore = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        List<PhotoFile> fileList = getTestListFiles();
        Order order = getNewOrderTest();

        dataSource.execUpdate("ALTER TABLE OrderPhotos RENAME COLUMN photoStatusId TO photoStatus");

        assertThrows(Exception.class, () -> {
            orderService.add(order, fileList);
        });

        dataSource.execUpdate("ALTER TABLE OrderPhotos RENAME COLUMN photoStatus TO photoStatusId");

        int cntOrdersAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM Orders");
        int cntPhotosAfter = dataSource.getResult("SELECT COUNT(*) cnt FROM OrderPhotos");

        assertEquals(cntOrdersBefore, cntOrdersAfter);
        assertEquals(cntPhotosBefore, cntPhotosAfter);

        File dir = new File(TEST_PATH_PHOTO);
        assertEquals(0, dir.list().length);
    }

    private List<PhotoFile> getTestListFiles() throws IOException {
        List<PhotoFile> fileList = new ArrayList<>(1);

        InputStream fakeStream = new ByteArrayInputStream("firstFile".getBytes());
        String fileName = "firstFile.txt";
        PhotoFile photoFile = new PhotoFile(fileName, fakeStream);
        fileList.add(photoFile);

        fakeStream = new ByteArrayInputStream("secondFile".getBytes());
        fileName = "secondFile.txt";
        photoFile = new PhotoFile(fileName, fakeStream);

        fileList.add(photoFile);

        return fileList;
    }


    private Order getNewOrderTest() {

        User user = new User();
        user.setId(1);

        Order.OrderBuilder orderBuilder = Order.builder()
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .user(user);

        return orderBuilder.build();
    }


    @AfterEach
    public void delete_folder() {
        File dir = new File(TEST_PATH_PHOTO);
        File[] dirOrders = dir.listFiles();
        if (dirOrders != null) {
            for (File dirOrder : dirOrders) {
                File[] files = dirOrder.listFiles();
                if (files != null) {
                    for (File insideFile : files) {
                        insideFile.delete();
                    }
                }
                dirOrder.delete();
            }
        }
    }


    @AfterAll
    public static void after() throws SQLException {
        dataSource.close();
        File dir = new File(TEST_PATH_PHOTO);
        if (dir.exists()) {
            dir.delete();
        }
    }

}
