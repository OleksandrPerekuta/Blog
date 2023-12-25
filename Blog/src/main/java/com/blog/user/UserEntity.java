package com.blog.user;

import com.blog.role.RoleEntity;
import com.blog.verificationToken.VerificationTokenEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String secondName;
    private String email;
    private String password;
    private boolean isEnabled;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<VerificationTokenEntity> tokens;
    @PrePersist
    private void setEnabled(){isEnabled=false;}
}
