package com.blog.comment.controller;

import com.blog.comment.dto.CommentDtoRequest;
import com.blog.comment.service.CommentService;
import com.blog.exception.CommentException;
import com.blog.exception.InsufficientAccessLevelException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;


@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id){
        try{
            return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);
        }catch (EntityActionVetoException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping
    public ResponseEntity<?> saveComment(@RequestBody @Valid CommentDtoRequest commentDtoRequest, Errors errors){
        if (errors.hasErrors()) {
            return new ResponseEntity<>(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        try{
            return new ResponseEntity<>(commentService.saveComment(commentDtoRequest,(UserDetails) authentication.getPrincipal()),HttpStatus.CREATED);
        }catch (EntityNotFoundException | InsufficientAccessLevelException | CommentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> setCommentTextRemoved(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            commentService.setCommentRemoved(id,(UserDetails) authentication.getPrincipal());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (InsufficientAccessLevelException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    //todo add so tree of comments would be retrieved
}