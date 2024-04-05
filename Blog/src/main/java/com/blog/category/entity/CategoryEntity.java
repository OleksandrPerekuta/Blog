package com.blog.category.entity;

import com.blog.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "categories", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private List<PostEntity> posts=new ArrayList<>();

}