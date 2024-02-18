package com.blog.tag.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class TagDtoCreation {
    @NotBlank(message = "name must not be null")
    private String name;
}
