package com.blog.user.mapper;

import com.blog.user.dto.AdminDtoRegister;
import com.blog.user.dto.UserDtoRegister;
import com.blog.user.dto.UserDtoResponse;
import com.blog.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDtoResponse mapToDto(UserEntity entity);

    UserEntity mapToEntity(UserDtoRegister userDtoRegister);

    UserEntity mapToEntity(AdminDtoRegister adminDtoRegister);

}
