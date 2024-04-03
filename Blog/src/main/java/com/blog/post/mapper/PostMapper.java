package com.blog.post.mapper;

import com.blog.category.dto.CategoryDtoCreation;
import com.blog.category.dto.CategoryDtoResponse;
import com.blog.category.entity.CategoryEntity;
import com.blog.category.mapper.CategoryMapperImpl;
import com.blog.post.dto.PostDtoRequest;
import com.blog.post.dto.PostDtoResponse;
import com.blog.post.entity.PostEntity;
import com.blog.tag.dto.TagDtoCreation;
import com.blog.tag.dto.TagDtoResponse;
import com.blog.tag.entity.TagEntity;
import com.blog.tag.mapper.TagMapperImpl;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.entity.UserEntity;
import com.blog.user.mapper.UserMapper;
import com.blog.user.mapper.UserMapperImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        UserMapperImpl.class,
        //UserEntity.class, UserDtoResponse.class,
        //CategoryEntity.class, CategoryDtoCreation.class, CategoryDtoResponse.class,
        //TagEntity.class, TagDtoCreation.class, TagDtoResponse.class
        CategoryMapperImpl.class,
        TagMapperImpl.class

})
public interface PostMapper {
    PostEntity mapToEntity(PostDtoRequest postDtoRequest);

    PostDtoResponse mapToDto(PostEntity postEntity);
}
