package com.blog.post.entity;

import com.blog.category.entity.CategoryEntity;
import com.blog.tag.entity.TagEntity;
import com.blog.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String text;
    private OffsetDateTime publishedAt;
    private boolean isEdited;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    public List<TagEntity> tags=new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "post_categories",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> categories=new ArrayList<>();

    @PrePersist
    private void setPublishedAt() {
        this.publishedAt = OffsetDateTime.now();
    }
}