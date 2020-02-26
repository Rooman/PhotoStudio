package com.photostudio.entity.user;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN"),
    GUEST("GUEST");

    private String name;

    UserRole(String name) {
        this.name = name;
    }

    public static UserRole getByUserRole(String name) {
        for (UserRole userRole : values()) {
            if (userRole.name.equalsIgnoreCase(name)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("No UserRole with name: " + name);
    }

    public String getName() {
        return name;
    }
}

