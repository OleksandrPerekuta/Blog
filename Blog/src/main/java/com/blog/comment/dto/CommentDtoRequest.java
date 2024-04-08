package com.blog.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CommentDtoRequest {
    private String text;
    private Long postId;
    private Long parentCommentId;
}
