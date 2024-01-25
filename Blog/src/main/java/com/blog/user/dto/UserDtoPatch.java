package com.blog.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDtoPatch {
    private String firstName;
    private String secondName;
    private String password;
}
