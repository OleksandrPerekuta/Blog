package com.blog.user.dto;

import com.blog.annotation.FieldsMatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@FieldsMatch(field = "password", matchingField = "confirmPassword", message = "Passwords do not match")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDtoPasswordReset {
    private String password;
    private String confirmPassword;
}
