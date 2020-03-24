package com.photostudio.entity.photo;

import java.util.Arrays;

public enum PhotoStatus {
    UNSELECTED(1, "UNSELECTED"),
    SELECTED(2, "SELECTED"),
    PAID(3, "PAID");

    private final int id;
    private final String name;

    PhotoStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PhotoStatus getByName(String photoStatusName) {

        return Arrays.stream(PhotoStatus.values())
                .filter(photoStatus -> photoStatus.name.equalsIgnoreCase(photoStatusName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PhotoStatus name is not correct!"));
    }

    public static PhotoStatus getById(int id) {
        return Arrays.stream(PhotoStatus.values())
                .filter(photoStatus -> (photoStatus.id == id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PhotoStatus id is not correct!"));
    }
}
