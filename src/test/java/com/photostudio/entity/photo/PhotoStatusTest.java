package com.photostudio.entity.photo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PhotoStatusTest {
    @Test
    void testGetByName() {
        PhotoStatus photoStatus = PhotoStatus.getByName("SELECTED");
        assertEquals(PhotoStatus.SELECTED, photoStatus);

        photoStatus = PhotoStatus.getByName("UNSELECTED");
        assertEquals(PhotoStatus.UNSELECTED, photoStatus);

        photoStatus = PhotoStatus.getByName("PAID");
        assertEquals(PhotoStatus.PAID, photoStatus);
    }

    @Test
    void testGetById() {
        PhotoStatus photoStatus = PhotoStatus.getById(2);
        assertEquals(PhotoStatus.SELECTED, photoStatus);

        photoStatus = PhotoStatus.getById(1);
        assertEquals(PhotoStatus.UNSELECTED, photoStatus);

        photoStatus = PhotoStatus.getById(3);
        assertEquals(PhotoStatus.PAID, photoStatus);

    }
}