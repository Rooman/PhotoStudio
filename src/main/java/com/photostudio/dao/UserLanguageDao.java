package com.photostudio.dao;

import com.photostudio.entity.user.UserLanguage;

import java.util.List;

public interface UserLanguageDao {
    List<UserLanguage> getAllLanguages();

    UserLanguage getLanguageById(int id);
}
