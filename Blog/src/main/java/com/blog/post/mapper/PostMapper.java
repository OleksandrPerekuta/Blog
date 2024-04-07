package com.blog.post.mapper;

import com.blog.category.mapper.CategoryMapperImpl;
import com.blog.post.dto.PostDtoRequest;
import com.blog.post.dto.PostDtoResponse;
import com.blog.post.entity.PostEntity;
import com.blog.tag.mapper.TagMapperImpl;
import com.blog.user.mapper.UserMapperImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        UserMapperImpl.class,
        CategoryMapperImpl.class,
        TagMapperImpl.class
})
public interface PostMapper {
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "category", ignore = true)
    PostEntity mapToEntity(PostDtoRequest postDtoRequest);

    PostDtoResponse mapToDto(PostEntity postEntity);
}
