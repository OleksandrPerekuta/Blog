package com.blog.post.mapper;

import com.blog.category.dto.CategoryDtoCreation;
import com.blog.category.entity.CategoryEntity;
import com.blog.post.dto.PostDtoRequest;
import com.blog.post.dto.PostDtoResponse;
import com.blog.post.entity.PostEntity;
import com.blog.tag.dto.TagDtoResponse;
import com.blog.tag.entity.TagEntity;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserEntity.class, UserDtoResponse.class, CategoryEntity.class, CategoryDtoCreation.class, TagDtoResponse.class, TagEntity.class})
public interface PostMapper {
    @Mapping(target = "tags", ignore = true)
    PostEntity mapToEntity(PostDtoRequest postDtoRequest);

    PostDtoResponse mapToDto(PostEntity postEntity);
}
