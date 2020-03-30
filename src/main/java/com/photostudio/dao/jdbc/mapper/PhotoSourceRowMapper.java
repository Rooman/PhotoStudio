package com.photostudio.dao.jdbc.mapper;

import com.photostudio.entity.photo.Photo;
import com.photostudio.entity.photo.PhotoStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoSourceRowMapper {
    public Photo mapRow(ResultSet resultSet) throws SQLException {
        String source = resultSet.getString("source");
        long id = resultSet.getLong("id");
        PhotoStatus photoStatus = PhotoStatus.getById(resultSet.getInt("photoStatusId"));
        return new Photo(id, source, photoStatus);
    }
}
