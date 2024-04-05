package com.blog.role.mapper;

import com.blog.role.dto.RoleDtoCreate;
import com.blog.role.dto.RoleDtoResponse;
import com.blog.role.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDtoResponse mapToDto(RoleEntity entity);

    RoleEntity mapToEntity(RoleDtoCreate dtoCreate);
}
