package com.photostudio.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhotoSourceRowMapper {
    public String mapRow(ResultSet resultSet) throws SQLException {
        return resultSet.getString("source");
    }
}
