package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.photo.Photo;
import com.photostudio.entity.photo.PhotoStatus;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class PhotoSourceRowMapperTest {
    @Test
    void testMapRow() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("source")).thenReturn("photo1.jpg");
        when(mockResultSet.getInt("photoStatusId")).thenReturn(1);

        PhotoSourceRowMapper photoSourceRowMapper = new PhotoSourceRowMapper();
        Photo photo = photoSourceRowMapper.mapRow(mockResultSet);

        assertEquals(1, photo.getId());
        assertEquals("photo1.jpg", photo.getSource());
        assertEquals(PhotoStatus.UNSELECTED, photo.getPhotoStatus());
    }

}