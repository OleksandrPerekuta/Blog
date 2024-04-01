package com.blog.tag.repository;

import com.blog.tag.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity,Long> {
    Optional<TagEntity> findByName(String name);
}
