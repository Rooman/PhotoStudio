package com.photostudio.web.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UIutil {

    public static String formatDate(LocalDateTime orderDate) {
        //    2020-01-29T18:38
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US);
        String formatDateTime = orderDate.format(formatter);
        return formatDateTime;
    }
}
