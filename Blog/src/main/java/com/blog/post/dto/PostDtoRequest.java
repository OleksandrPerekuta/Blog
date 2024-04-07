package com.blog.post.dto;

import com.blog.tag.dto.TagDtoCreation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @NotBlank(message = "category must not be null")
    private String category;
    private Set<TagDtoCreation> tags;
}
