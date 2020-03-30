package com.photostudio.security.impl;

import com.photostudio.entity.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSecurityServiceTest {

    DefaultSecurityService securityService = new DefaultSecurityService(null, null);

    @Test
    void testGeneratePasswordWithIncorrectCount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> securityService.generatePassword(0));
    }

    @Test
    void testGeneratedPassword() {
        String generatePassword = securityService.generatePassword(10);
        assertEquals(10, generatePassword.length());
        for (char value : generatePassword.toCharArray()) {
            assertTrue(Character.isAlphabetic(value) || Character.isDigit(value));

            if (Character.isAlphabetic(value)) {
                assertTrue(Character.isUpperCase(value));
            }
        }
    }

    @Test
    void testIsTrueOldPassword() {
        //prepare
        String correctPassword = "12345";
        User user = new User();
        user.setSalt("3d47ccde-5b58-4c7b-a84c-28c27d566f8e");
        user.setPasswordHash("8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257");

        //then
        assertTrue(securityService.isOldPassword(correctPassword, user));
    }

    @Test
    void testIsFalseOldPassword() {
        //prepare
        String notCorrectPassword = "1234567";
        User user = new User();
        user.setSalt("3d47ccde-5b58-4c7b-a84c-28c27d566f8e");
        user.setPasswordHash("8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257");

        //then
        assertFalse(securityService.isOldPassword(notCorrectPassword, user));
    }
}