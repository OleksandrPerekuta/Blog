package com.blog.user_role.service;

import com.blog.role.entity.RoleEntity;
import com.blog.user.entity.UserEntity;
import com.blog.user_role.entity.UserRoleEntity;
import com.blog.user_role.entity.UserRoleId;
import com.blog.user_role.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRoleService {
    private UserRoleRepository userRoleRepository;
    public UserRoleEntity setUserRole(UserEntity user, RoleEntity role){
        return userRoleRepository.save(new UserRoleEntity(new UserRoleId(user.getId(),role.getId()),user,role));
    }
    public UserRoleEntity getUserRole(UserEntity user, RoleEntity role){
        return userRoleRepository.getByUserAndRole(user,role)
                .orElseThrow(()->new EntityNotFoundException("user_role is not found for user_id: "+user.getId()+" and role_id: "+role.getId()));
    }
}
