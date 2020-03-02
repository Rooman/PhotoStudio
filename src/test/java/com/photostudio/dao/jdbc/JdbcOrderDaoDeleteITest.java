package com.photostudio.dao.jdbc;


import com.photostudio.dao.file.LocalDiskPhotoDao;
import com.photostudio.dao.jdbc.testUtils.TestDataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcOrderDaoDeleteITest {
    private static TestDataSource dataSource = new TestDataSource();
    private static JdbcDataSource jdbcDataSource;

    private final String TEST_PATH_PHOTO = "test_delete_orders";

    @BeforeAll
    public static void setUp() throws SQLException {
        jdbcDataSource = dataSource.init();
        dataSource.runScript("db/clear_orders.sql");
        dataSource.runScript("db/insert_user_data.sql");
    }

    @BeforeEach
    public void before() throws SQLException, IOException {
        dataSource.runScript("db/data_delete_order.sql");
    }

    @Test
    public void testDeleteOrderWithoutPhotos() {
        //before
        int cntOrdersBefore = dataSource.getResult("SELECT COUNT(*) CNT FROM Orders");

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.delete(1);

        //after
        int cntOrder1After = dataSource.getResult("SELECT COUNT(*) CNT FROM Orders WHERE id = 1");
        int cntOrdersAfter = dataSource.getResult("SELECT COUNT(*) CNT FROM Orders");

        assertEquals(cntOrdersBefore - 1, cntOrdersAfter);
        assertEquals(0, cntOrder1After);

    }

    @Test
    public void testDeleteNotExistingOrder() {
        //before
        int cntOrdersBefore = dataSource.getResult("SELECT COUNT(*) CNT FROM Orders");

        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.delete(11);

        int cntOrdersAfter = dataSource.getResult("SELECT COUNT(*) CNT FROM Orders");
        assertEquals(cntOrdersBefore, cntOrdersAfter);
    }

    @Test
    public void testDeletePhotoOrder() {
        //before
        int cntPhotosBefore = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrder = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=2");
        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.delete(2);
        //after
        int cntPhotosAfter = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrderAfter = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=2");

        assertEquals(0, cntPhotosByOrderAfter);
        assertEquals(cntPhotosBefore - cntPhotosByOrder, cntPhotosAfter);

    }

    @Test
    public void testDeletePhotoFromDisk() throws IOException {
        //before
        int cntPhotosBefore = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrder = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=3");

        //create dirs and files
        String path = TEST_PATH_PHOTO + File.separator + "3";
        File dir = new File(path);
        dir.mkdirs();
        for (int i = 1; i <= cntPhotosByOrder; i++) {
            File newFile = new File(path, String.valueOf(i) + ".jpg");
            newFile.createNewFile();
        }

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.delete(3);

        LocalDiskPhotoDao photoDao = new LocalDiskPhotoDao(TEST_PATH_PHOTO);
        photoDao.deleteByOrder(3);

        //after
        int cntPhotosAfter = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrderAfter = dataSource.getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=3");

        assertEquals(0, cntPhotosByOrderAfter);
        assertEquals(cntPhotosBefore - cntPhotosByOrder, cntPhotosAfter);

        File dirAfter = new File(path);
        assertEquals(false, dirAfter.exists());
    }

    @Test
    public void testDeletePhotoFromEmptyFolder() throws IOException {
        //create dirs, but no files
        String path = TEST_PATH_PHOTO + File.separator + "3";
        File dir = new File(path);
        dir.mkdirs();

        LocalDiskPhotoDao photoDao = new LocalDiskPhotoDao(TEST_PATH_PHOTO);
        photoDao.deleteByOrder(3);

        File dirAfter = new File(path);
        assertEquals(false, dirAfter.exists());
    }

    @AfterEach
    public void after() throws SQLException {
        dataSource.runScript("db/clear_orders.sql");
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        dataSource.close();
    }

}
