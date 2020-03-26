package com.photostudio.entity.user;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserLanguage {
    private int id;
    private String shortName;
    private String fullName;
}
