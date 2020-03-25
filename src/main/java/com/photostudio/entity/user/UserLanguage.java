package com.photostudio.entity.user;

import java.util.Arrays;

public enum UserLanguage {
    private final int id;
    private final String short_name;
    private final String full_name;

    UserLanguage(int id, String short_name, String full_name) {
        this.id = id;
        this.short_name = short_name;
        this.full_name = full_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static UserLanguage getByShortName(String short_name) {

        return Arrays.stream(UserLanguage.values())
                .filter(userLanguage -> userLanguage.short_name.equalsIgnoreCase(userLanguage))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Language short name is not correct!"));
    }

    public static UserLanguage getById(int id) {
        return Arrays.stream(UserLanguage.values())
                .filter(userLanguage -> (userLanguage.id == id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Language id is not correct!"));
    }
}
