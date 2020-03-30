package com.photostudio.web.util;

import org.apache.commons.codec.digest.DigestUtils;

public final class UtilClass {

    public static String getHashedString(String originalString, String salt) {
        String saltPassword = originalString + salt;
        byte[] saltedStringBytes = saltPassword.getBytes();
        return DigestUtils.sha256Hex(saltedStringBytes);
    }
}
