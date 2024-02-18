package com.blog.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CategoryDtoCreation {
    @NotBlank(message = "name must not be null")
    private String name;
}
