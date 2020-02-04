package com.photostudio.security.entity;

import com.photostudio.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Session {
    private String token;
    private User user;
    private LocalDateTime expireDate;
}
