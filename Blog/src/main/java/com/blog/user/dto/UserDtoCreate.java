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
public class UserDtoCreate {
    @NotBlank(message = "first name must not be null")
    private String firstName;
    @NotBlank(message = "second name must not be null")
    private String secondName;
    @NotBlank(message = "email must not be null")
    @Email
    private String email;
    @NotBlank(message = "password must not be null")
    private String password;
}
