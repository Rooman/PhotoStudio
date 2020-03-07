package com.photostudio.security.impl;

import com.photostudio.entity.user.User;
import com.photostudio.exception.LoginPasswordInvalidException;
import com.photostudio.security.entity.Session;
import com.photostudio.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultSecurityServiceTest {
//    private DefaultSecurityService securityService = new DefaultSecurityService();
//
//    @Test
//    public void testGedHashPassword() {
//        //prepare
//        String password = "12345";
//        String salt = "3d47ccde-5b58-4c7b-a84c-28c27d566f8e";
//        String expectedHashedPassword = "8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257";
//
//        //when
//        String actualHashedPassword = securityService.getHashedPassword(salt, password);
//
//        //then
//        assertEquals(expectedHashedPassword, actualHashedPassword);
//    }
//
//    @Test
//    public void testLogin() {
//        //prepare
//        User user = new User();
//        user.setSalt("3d47ccde-5b58-4c7b-a84c-28c27d566f8e");
//        user.setPasswordHash("8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257");
//        user.setId(1);
//        user.setEmail("mymail@d.com");
//
//        UserService userService = mock(UserService.class);
//        when(userService.getUserByLogin("mymail@d.com")).thenReturn(user);
//
//        Session expectedSession = Session.builder()
//                .user(user).build();
//
//        securityService.setUserService(userService);
//
//        //when
//        Session actualSession = securityService.login("mymail@d.com", "12345");
//
//        //then
//        assertNotNull(actualSession);
//        assertEquals(expectedSession.getUser().getId(), actualSession.getUser().getId());
//        assertNotNull(actualSession.getToken());
//        assertNotNull(actualSession.getExpireDate());
//    }
//
//    @Test
//    public void testLoginIncorrectPassword() {
//        //prepare
//        User user = new User();
//        user.setSalt("3d47ccde-5b58-4c7b-a84c-28c27d566f8e");
//        user.setPasswordHash("8bbefdbdeea504b1d886d071d071cc02eba8fd06cef7fe735a241107db052257");
//        user.setId(1);
//        user.setEmail("mymail@d.com");
//
//        UserService userService = mock(UserService.class);
//        when(userService.getUserByLogin("mymail@d.com")).thenReturn(user);
//
//        securityService.setUserService(userService);
//
//        //then
//        Assertions.assertThrows(LoginPasswordInvalidException.class, () -> {
//            securityService.login("mymail@d.com", "IncorrectPassword");
//        });
//    }
//
//    @Test
//    public void testLoginIncorrectEmail() {
//        //prepare
//        UserService userService = mock(UserService.class);
//        when(userService.getUserByLogin("mymail@d.com")).thenReturn(null);
//
//        securityService.setUserService(userService);
//
//        //then
//        Assertions.assertThrows(LoginPasswordInvalidException.class, () -> {
//            securityService.login("mymail@d.com", "correctPassword");
//        });
//    }
}
