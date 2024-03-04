package com.blog.post.mapper;

import com.blog.post.dto.PostDtoRequest;
import com.blog.post.dto.PostDtoResponse;
import com.blog.post.entity.PostEntity;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserEntity.class, UserDtoResponse.class})
public interface PostMapper {
    PostEntity mapToEntity(PostDtoRequest postDtoRequest);

    PostDtoResponse mapToDto(PostEntity postEntity);
}
