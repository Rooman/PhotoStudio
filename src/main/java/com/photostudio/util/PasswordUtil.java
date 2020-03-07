package com.photostudio.util;

import java.security.SecureRandom;

public class PasswordUtil {

    public String getSalt(){
        SecureRandom secureRandom = new SecureRandom();
        byte [] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return bytes.toString();
    }
}
