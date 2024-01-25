package com.blog.verificationToken.dto;

import com.blog.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VerificationTokenDtoResponse {
    private Long id;
    private OffsetDateTime expirationDate;
    private String token;
    private UserEntity user;
}
