package com.blog.tag.mapper;

import com.blog.tag.dto.TagDtoCreation;
import com.blog.tag.dto.TagDtoResponse;
import com.blog.tag.entity.TagEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDtoResponse mapToDto(TagEntity tagEntity);

    TagEntity mapToEntity(TagDtoCreation tagDtoCreation);
}
