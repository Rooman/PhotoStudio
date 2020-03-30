package com.photostudio.security.entity;

import com.photostudio.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class Session {
    private String token;
    private User user;
    private LocalDateTime expireDate;
}
