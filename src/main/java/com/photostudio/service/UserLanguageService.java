package com.photostudio.service;

import com.photostudio.entity.user.UserLanguage;

import java.util.List;

public interface UserLanguageService {
    List<UserLanguage> getAllLanguages();

    UserLanguage getLanguageById(int id);
}
