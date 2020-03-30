package com.photostudio.web.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilClassTest {

    @Test
    void testGetHashedString() {
                //prepare
        String password = "12345";
        String salt = "3d47ccde-5b58-4c7b-a84c-28c27d566f8e";
        String expectedHashedPassword = "8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257";

        //when
        String actualHashedPassword = UtilClass.getHashedString(password, salt);

        //then
        assertEquals(expectedHashedPassword, actualHashedPassword);
    }
}