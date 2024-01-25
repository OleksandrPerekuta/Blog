package com.blog.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class RoleDtoCreate {
    @NotBlank(message = "name must not be null")
    private String name;
}


