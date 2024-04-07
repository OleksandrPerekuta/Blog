package com.blog.category.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CategoryDtoResponse {
    private Long id;
    private String name;
    private boolean isActive;
}
