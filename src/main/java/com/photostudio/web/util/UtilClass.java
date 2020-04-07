package com.photostudio.web.util;

import org.apache.commons.codec.digest.DigestUtils;

public final class UtilClass {

    public static String getHashedString(String originalString, String salt) {
        String saltPassword = originalString + salt;
        byte[] saltedStringBytes = saltPassword.getBytes();
        return DigestUtils.sha256Hex(saltedStringBytes);
    }

    public static boolean isChanged(String newValue, String oldValue) {
        if (newValue != null) {
            return !newValue.equals(oldValue);
        } else {
            if (oldValue == null) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static int getIdFromPath(String uri) {
        return Integer.parseInt(uri.substring(uri.lastIndexOf("/") + 1));
    }
}
