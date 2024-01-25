package com.blog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDtoResponse {
    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private String password;
    private boolean isEnabled;
}
