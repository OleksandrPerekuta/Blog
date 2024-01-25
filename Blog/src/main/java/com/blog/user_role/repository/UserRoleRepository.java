package com.blog.user_role.repository;

import com.blog.role.entity.RoleEntity;
import com.blog.user.entity.UserEntity;
import com.blog.user_role.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity,Long> {
    Optional<UserRoleEntity> getByUserAndRole(UserEntity user, RoleEntity role);
}