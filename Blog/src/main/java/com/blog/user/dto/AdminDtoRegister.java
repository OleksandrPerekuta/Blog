package com.blog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminDtoRegister {
    @NotBlank(message = "username must not be null")
    private String username;
    @NotBlank(message = "email must not be null")
    @Email(message = "incorrect email format")
    private String email;
    @NotBlank(message = "password must not be null")
    private String password;
    @NotBlank(message = "secret is incorrect")
    private String secret;
}
