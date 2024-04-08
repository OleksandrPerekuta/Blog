package com.blog.comment.mapper;

import com.blog.comment.dto.CommentDtoRequest;
import com.blog.comment.dto.CommentDtoResponse;
import com.blog.comment.entity.CommentEntity;
import com.blog.user.mapper.UserMapperImpl;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = UserMapperImpl.class)
public interface CommentMapper {
    CommentDtoResponse mapToDto(CommentEntity entity);
    CommentEntity mapToEntity(CommentDtoRequest dto);
}