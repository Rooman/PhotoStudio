package com.photostudio.security.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}