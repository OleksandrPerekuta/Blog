package com.blog.post.dto;

import com.blog.category.dto.CategoryDtoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class PostDtoRequest {
    @NotBlank(message = "title must not be null")
    private String title;
    @NotBlank(message = "text must not be null")
    @Size(min = 10, message = "text must be at least 10 characters")
    private String text;
    private Set<CategoryDtoResponse> categories;
    private List<String> tags;
}
