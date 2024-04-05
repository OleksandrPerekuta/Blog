package com.blog.category.mapper;

import com.blog.category.dto.CategoryDtoCreation;
import com.blog.category.dto.CategoryDtoResponse;
import com.blog.category.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDtoResponse mapToDto(CategoryEntity categoryEntity);

    CategoryEntity mapToEntity(CategoryDtoCreation categoryDtoCreation);
}
