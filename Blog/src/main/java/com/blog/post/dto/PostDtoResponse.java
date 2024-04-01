package com.blog.post.dto;

import com.blog.category.entity.CategoryEntity;
import com.blog.tag.entity.TagEntity;
import com.blog.user.dto.UserDtoResponse;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class PostDtoResponse {
    private Long id;
    private String title;
    private String text;
    private OffsetDateTime publishedAt;
    private boolean isEdited;
    private UserDtoResponse user;
    private Set<TagEntity> tags;
    private Set<CategoryEntity> categories;

}
