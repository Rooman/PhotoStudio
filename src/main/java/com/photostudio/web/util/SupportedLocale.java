package com.photostudio.web.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
public enum SupportedLocale {

    GERMANY("de", Locale.forLanguageTag("de-DE")),
    RUSSIAN("ru", Locale.forLanguageTag("ru-RU")),
    US("en", Locale.US);

    private String name;
    private Locale locale;

    SupportedLocale(String name, Locale locale) {
        this.name = name;
        this.locale = locale;
    }

    public static SupportedLocale findByName(String name) {
        for (SupportedLocale value : values()) {
            if (value.name.equalsIgnoreCase(name)) {
                log.info("Found locale for name {}", name);
                return value;
            }
        }
        log.info("Not found locale for name {}, default locale returned {}", name, US);

        return US;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SupportedLocale{" +
                "name='" + name + '\'' +
                ", locale=" + locale +
                '}';
    }
}
