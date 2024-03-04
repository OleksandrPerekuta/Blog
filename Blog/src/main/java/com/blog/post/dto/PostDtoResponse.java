package com.blog.post.dto;

import com.blog.user.dto.UserDtoResponse;
import lombok.*;

import java.time.OffsetDateTime;

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
}
