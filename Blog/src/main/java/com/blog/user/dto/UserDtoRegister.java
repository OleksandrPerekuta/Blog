package com.blog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDtoRegister {
    @NotBlank(message = "username must not be null")
    private String username;
    @NotBlank(message = "email must not be null")
    @Email(message = "incorrect email format")
    private String email;
    @NotBlank(message = "password must not be null")
    private String password;
}
