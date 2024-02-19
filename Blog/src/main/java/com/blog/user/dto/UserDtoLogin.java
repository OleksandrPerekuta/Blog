package com.blog.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoLogin {
    private String email;
    private String password;
}
