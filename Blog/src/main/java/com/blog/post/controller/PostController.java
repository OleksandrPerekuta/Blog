package com.blog.post.controller;

import com.blog.exception.InsufficientAccessLevelException;
import com.blog.post.dto.PostDtoRequest;
import com.blog.post.dto.PostDtoResponse;
import com.blog.post.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try{
            return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> savePost(@RequestBody @Valid PostDtoRequest postDtoRequest, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage),HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(postService.savePost(postDtoRequest, (UserDetails) authentication.getPrincipal()),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            postService.deletePost(id, (UserDetails) authentication.getPrincipal());
        }catch (InsufficientAccessLevelException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostDtoResponse>> getPostsByUsername(@PathVariable String username){
        return new ResponseEntity<>(postService.getPostsByUsername(username),HttpStatus.OK);
    }
}
