package com.blog.comment.dto;

import com.blog.user.dto.UserDtoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoResponse {
    private Long id;
    private String text;
    private UserDtoResponse user;
    private Long postId;
    private Long parentCommentId;
}