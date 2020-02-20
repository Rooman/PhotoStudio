package com.photostudio.entity.user;

import lombok.*;

@Data
public class User {
    private long id;
    private String email;
    private UserRole userRole;
    private String passwordHash;
    private String salt;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private int zip;
    private String address;
    private String title;
    private String additionalInfo;
}
