package com.blog.user_role.entity;
import com.blog.role.entity.RoleEntity;
import com.blog.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity(name = "user_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}