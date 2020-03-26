package com.photostudio.dao.jdbc;


import com.photostudio.dao.UserLanguageDao;
import com.photostudio.dao.jdbc.mapper.LanguageRowMapper;
import com.photostudio.entity.user.UserLanguage;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JdbcUserLanguageCachedDao implements UserLanguageDao {

    private static final String GET_ALL_LANGUAGES = "SELECT l.id, l.shortName, l.fullName FROM Languages l ORDER BY l.shortName";
    Map<Integer, UserLanguage> languages;

    public JdbcUserLanguageCachedDao(DataSource dataSource) {
        load(dataSource);
    }

    @Override
    public List<UserLanguage> getAllLanguages() {
        return languages.values().stream().collect(Collectors.toList());
    }

    @Override
    public UserLanguage getLanguageById(int id) {
        return languages.get(id);
    }

    void load(DataSource dataSource) {
        log.info("Load languages cache");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_LANGUAGES)) {
            Map<Integer, UserLanguage> loadedLanguages = new HashMap<>();
            while (resultSet.next()) {
                UserLanguage userLanguage = LanguageRowMapper.mapRow(resultSet);
                loadedLanguages.put(userLanguage.getId(), userLanguage);
            }
            languages = Collections.unmodifiableMap(loadedLanguages);
        } catch (SQLException e) {
            log.error("Get languages cache error", e);
            throw new RuntimeException("Get languages cache error", e);
        }
    }

}
