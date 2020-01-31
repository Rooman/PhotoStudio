package com.photostudio.entity.user;

public enum Gender {
    FEMALE("FEMALE"),
    MALE("MALE");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    public static Gender getByGender(String name) {
        for (Gender gender : values()) {
            if (gender.name.equalsIgnoreCase(name)) {
                return gender;
            }
        }
        return null;
    }
}

