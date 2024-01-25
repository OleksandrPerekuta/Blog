package com.blog.verificationToken.repository;

import com.blog.verificationToken.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity,Long> {
    Optional<VerificationTokenEntity> findByToken(String token);
    void deleteByUserId(Long userId);
}
