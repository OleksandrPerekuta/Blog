package com.blog.user.mapper;

import com.blog.user.dto.UserDtoCreate;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDtoResponse mapToDto(UserEntity entity);
    UserEntity mapToEntity(UserDtoCreate userDtoCreate);
}
