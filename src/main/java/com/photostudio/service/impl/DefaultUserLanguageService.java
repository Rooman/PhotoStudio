package com.photostudio.service.impl;

import com.photostudio.dao.UserLanguageDao;
import com.photostudio.entity.user.UserLanguage;
import com.photostudio.service.UserLanguageService;

import java.util.List;

public class DefaultUserLanguageService implements UserLanguageService {
    private UserLanguageDao userLanguageDao;

    public DefaultUserLanguageService(UserLanguageDao userLanguageDao) {
        this.userLanguageDao = userLanguageDao;
    }

    @Override
    public List<UserLanguage> getAllLanguages() {
        return userLanguageDao.getAllLanguages();
    }

    @Override
    public UserLanguage getLanguageById(int id) {
        return userLanguageDao.getLanguageById(id);
    }
}
