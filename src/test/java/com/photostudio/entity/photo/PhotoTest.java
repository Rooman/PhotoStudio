package com.photostudio.entity.photo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {

    @Test
    void testGetName() {
        Photo photo = new Photo(1, "testPicture.jpg", PhotoStatus.UNSELECTED);
        String expectedName = "testPicture";

        String actualName = photo.getName();

        assertEquals(expectedName, actualName);
    }
}