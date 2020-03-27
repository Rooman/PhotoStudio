package com.photostudio.dao.jdbc.mapper;


import com.photostudio.entity.user.UserLanguage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageRowMapper {

    public static UserLanguage mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String shortName = resultSet.getString("shortName");
        String fullName = resultSet.getString("fullName");
        return new UserLanguage(id, shortName, fullName);
    }
}
