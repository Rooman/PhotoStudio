package com.photostudio.entity;

import java.util.Objects;

public enum Gender {
    FEMALE("FEMALE"),
    MALE("MALE");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    public static Gender getByGender(String name) {
        for (Gender gender : values()) {
            if (!Objects.equals(gender.name.equalsIgnoreCase(name), null)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("No such gender : " + name);
    }
}

