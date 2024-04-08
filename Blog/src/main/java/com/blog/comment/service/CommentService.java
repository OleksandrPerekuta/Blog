package com.blog.comment.service;

import com.blog.comment.dto.CommentDtoRequest;
import com.blog.comment.dto.CommentDtoResponse;
import com.blog.comment.entity.CommentEntity;
import com.blog.comment.mapper.CommentMapper;
import com.blog.comment.repository.CommentRepository;
import com.blog.exception.CommentException;
import com.blog.exception.InsufficientAccessLevelException;
import com.blog.post.entity.PostEntity;
import com.blog.post.repository.PostRepository;
import com.blog.role.service.RoleService;
import com.blog.user.entity.UserEntity;
import com.blog.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private RoleService roleService;

    @Transactional
    public CommentDtoResponse getCommentById(Long id) throws EntityNotFoundException {
        CommentEntity entity = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        CommentDtoResponse commentDtoResponse = commentMapper.mapToDto(entity);
        commentDtoResponse.setPostId(entity.getPost().getId());
        if (entity.getParentComment() != null) {
            commentDtoResponse.setParentCommentId(entity.getParentComment().getId());
        }else{
            commentDtoResponse.setParentCommentId(null);
        }
        return commentDtoResponse;
    }

    @Transactional
    public CommentDtoResponse saveComment(CommentDtoRequest commentDtoRequest, UserDetails userDetails) throws InsufficientAccessLevelException, CommentException {
        CommentEntity commentEntity = commentMapper.mapToEntity(commentDtoRequest);
        UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + userDetails.getUsername()));
        if (!userEntity.isEnabled()) {
            throw new InsufficientAccessLevelException("Activate your account to perform activity");
        }
        commentEntity.setUser(userEntity);
        PostEntity postEntity = postRepository.findById(commentDtoRequest.getPostId()).orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + commentDtoRequest.getPostId()));
        if (!postEntity.getCategory().isActive()) {
            throw new InsufficientAccessLevelException("Comments cannot be added to a post in the closed category");
        }
        commentEntity.setPost(postEntity);
        //todo: clean code
        //check if parent comment and this comment have the same post id
        if (commentDtoRequest.getParentCommentId() != null) {
            Optional<CommentEntity> parent = commentRepository.findById(commentDtoRequest.getParentCommentId());
            if (parent.isPresent()) {
                if (parent.get().getPost().getId() != commentDtoRequest.getPostId()) {
                    throw new CommentException("Post id does not match");
                }
                commentEntity.setParentComment(parent.get());
            } else {
                commentEntity.setParentComment(null);
            }
        } else {
            commentEntity.setParentComment(null);
        }
        commentRepository.save(commentEntity);
        CommentDtoResponse commentDtoResponse = commentMapper.mapToDto(commentEntity);
        if (commentEntity.getParentComment() != null) {
            commentDtoResponse.setParentCommentId(commentEntity.getParentComment().getId());
        } else {
            commentDtoResponse.setParentCommentId(null);
        }
        commentDtoResponse.setPostId(commentEntity.getPost().getId());
        return commentDtoResponse;
    }

    @Transactional
    public void setCommentRemoved(Long id, UserDetails userDetails) throws InsufficientAccessLevelException {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        boolean isUserMatched = userDetails.getUsername().equals(commentEntity.getPost().getUser().getUsername());
        if (!(isUserMatched || userDetails.getAuthorities().contains(roleService.getRoleEntityByName("ROLE_ADMIN")))) {
            throw new InsufficientAccessLevelException("You do not have enough permissions for this activity");
        }
        commentEntity.setText("[removed]");
    }
}