package com.photostudio.security.impl;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.security.entity.Session;
import com.photostudio.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

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
    void testGetSessionWithNullToken() {
        Session session = securityService.getSession(null);

        assertNull(session);
    }

    @Test
    void testGetSessionWithInvalidToken() {
        Session session = securityService.getSession(UUID.randomUUID().toString());

        assertNull(session);
    }

    @Test
    void testGetSession() {
        //prepare
        User user = new User();
        user.setId(1);
        user.setEmail("user@test.com");
        String userToken = UUID.randomUUID().toString();
        securityService.addNewSession(user, userToken);
        //when
        Session session = securityService.getSession(userToken);
        //then
        assertNotNull(session);
        assertEquals(user.getEmail(), session.getUser().getEmail());
    }
}