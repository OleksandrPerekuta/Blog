package com.blog.post.service;

import com.blog.category.entity.CategoryEntity;
import com.blog.category.service.CategoryService;
import com.blog.exception.CategoryException;
import com.blog.exception.InsufficientAccessLevelException;
import com.blog.post.dto.PostDtoRequest;
import com.blog.post.dto.PostDtoResponse;
import com.blog.post.entity.PostEntity;
import com.blog.post.mapper.PostMapper;
import com.blog.post.repository.PostRepository;
import com.blog.role.service.RoleService;
import com.blog.tag.service.TagService;
import com.blog.user.entity.UserEntity;
import com.blog.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final RoleService roleService;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostDtoResponse getPostById(Long id) throws EntityNotFoundException {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        return postMapper.mapToDto(postEntity);
    }

    @Transactional
    public PostDtoResponse savePost(PostDtoRequest postDtoRequest, UserDetails userDetails) throws EntityNotFoundException, CategoryException {
        PostEntity postEntity = postMapper.mapToEntity(postDtoRequest);
        postEntity.setUser(userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + userDetails.getUsername())));
        postEntity.setTags(
                postDtoRequest.getTags()
                        .stream()
                        .map(tag -> tagService.getOrCreateByName(tag.getName()))
                        .collect(Collectors.toSet()));
        CategoryEntity entity =categoryService.getEntityByName(postDtoRequest.getCategory());
        if (entity.isActive()){
            postEntity.setCategory(entity);
        }else{
            throw new CategoryException("Post cannot be added to a inactive category");
        }
        postEntity = postRepository.save(postEntity);
        return postMapper.mapToDto(postEntity);
    }

    @Transactional
    public void deletePost(Long id, UserDetails userDetails) throws InsufficientAccessLevelException {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        boolean isUserMatched = userDetails.getUsername().equals(postEntity.getUser().getUsername());
        if (!(isUserMatched || userDetails.getAuthorities().contains(roleService.getRoleEntityByName("ROLE_ADMIN")))) {
            throw new InsufficientAccessLevelException("You do not have enough permissions for this activity");
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public List<PostDtoResponse> getPostsByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + username));
        return user.getPosts().stream().map(postMapper::mapToDto).toList();
    }
}