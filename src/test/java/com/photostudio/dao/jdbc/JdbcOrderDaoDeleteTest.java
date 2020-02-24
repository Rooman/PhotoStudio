package com.photostudio.dao.jdbc;


import com.photostudio.dao.LocalDiskPhotoDao;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcOrderDaoDeleteTest {
    private Connection connection;
    private JdbcDataSource jdbcDataSource;
    private final String TEST_PATH_PHOTO = "C:/test_delete_orders";

    private int getResult(String sqlQuery) {
        int result = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)
        )
        {
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException ex) {
           // System.out.println("Error in the check before test:", ex.getMessage());
        }
       return result;
    }

    @BeforeEach
    public void before() throws SQLException, IOException {
        jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:photostudio;MODE=MySQL");
        jdbcDataSource.setUser("h2");
        jdbcDataSource.setPassword("h2");

        connection = jdbcDataSource.getConnection();

        FileReader fileSchema = new FileReader(getClass().getClassLoader().getResource("db/schema.sql").getFile());

        RunScript.execute(connection, fileSchema);

        FileReader fileData1 = new FileReader(getClass().getClassLoader().getResource("db/migration/V1_5__insert_UserRole.sql").getFile());
        RunScript.execute(connection, fileData1);
        fileData1.close();

        FileReader fileData2 = new FileReader(getClass().getClassLoader().getResource("db/migration/V1_6__insert_OrderStatus.sql").getFile());
        RunScript.execute(connection, fileData2);
        fileData2.close();

        FileReader fileData3 = new FileReader(getClass().getClassLoader().getResource("db/migration/V1_7__insert_PhotoStatus.sql").getFile());
        RunScript.execute(connection, fileData3);
        fileData3.close();

        FileReader fileData4 = new FileReader(getClass().getClassLoader().getResource("db/migration/V1_8__insert_admin.sql").getFile());
        RunScript.execute(connection, fileData4);
        fileData4.close();

        FileReader fileData5 = new FileReader(getClass().getClassLoader().getResource("db/data_delete_order.sql").getFile());
        RunScript.execute(connection, fileData5);
        fileData5.close();
    }

    @Test
    public void testDeleteOrderWithoutPhotos() {
        //before
        int cntOrdersBefore = getResult("SELECT COUNT(*) CNT FROM Orders");

       //when
          JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
          jdbcOrderDao.delete(1);

        //after
        int cntOrder1After = getResult("SELECT COUNT(*) CNT FROM Orders WHERE id = 1");
        int cntOrdersAfter = getResult("SELECT COUNT(*) CNT FROM Orders");

        assertEquals(cntOrdersBefore-1, cntOrdersAfter);
        assertEquals(0, cntOrder1After);

    }

    @Test
    public void testDeleteNotExistingOrder() {
        //before
        int cntOrdersBefore = getResult("SELECT COUNT(*) CNT FROM Orders");

        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.delete(10);

        int cntOrdersAfter = getResult("SELECT COUNT(*) CNT FROM Orders");
        assertEquals(cntOrdersBefore, cntOrdersAfter);
    }

    @Test
    public void testDeletePhotoOrder() {
        //before
        int cntPhotosBefore = getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrder = getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=2");
        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.delete(2);
        //after
        int cntPhotosAfter = getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrderAfter = getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=2");

        assertEquals(0, cntPhotosByOrderAfter);
        assertEquals(cntPhotosBefore-cntPhotosByOrder, cntPhotosAfter);

    }

    @Test
    public void testDeletePhotoFromDisk() throws IOException {
        //before
        int cntPhotosBefore = getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrder = getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=3");

        //create dirs and files
        String path = TEST_PATH_PHOTO + File.separator + "3";
        File dir=new File(path);
        dir.mkdirs();
        for (int i=1; i <= cntPhotosByOrder; i++) {
            File newFile=new File(path, String.valueOf(i)+".jpg");
            newFile.createNewFile();
        }

        //when
        JdbcOrderDao jdbcOrderDao = new JdbcOrderDao(jdbcDataSource);
        jdbcOrderDao.delete(3);

        LocalDiskPhotoDao photoDao=new LocalDiskPhotoDao(TEST_PATH_PHOTO);
        photoDao.deleteByOrder(3);

        //after
        int cntPhotosAfter = getResult("SELECT COUNT(*) CNT FROM OrderPhotos");
        int cntPhotosByOrderAfter = getResult("SELECT COUNT(*) CNT FROM OrderPhotos WHERE orderId=3");

        assertEquals(0, cntPhotosByOrderAfter);
        assertEquals(cntPhotosBefore-cntPhotosByOrder, cntPhotosAfter);

        File dirAfter = new File(path);
        assertEquals(false,dirAfter.exists());

    }

    @Test
    public void testDeletePhotoFromEmptyFolder() throws IOException {
        //create dirs, but no files
        String path = TEST_PATH_PHOTO + File.separator + "3";
        File dir=new File(path);
        dir.mkdirs();

        LocalDiskPhotoDao photoDao=new LocalDiskPhotoDao(TEST_PATH_PHOTO);
        photoDao.deleteByOrder(3);

        File dirAfter = new File(path);
        assertEquals(false,dirAfter.exists());


    }

        @AfterEach
    public void after() throws SQLException {
        connection.close();
    }

}
