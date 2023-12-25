package com.blog.verificationToken;

import com.blog.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity(name = "verification_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime expirationDate;
    private String token;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @PrePersist
    private void setExpiratioDate(){
        expirationDate= OffsetDateTime.now().plusDays(1);
    }
}
