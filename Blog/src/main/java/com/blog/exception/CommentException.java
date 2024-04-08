package com.blog.exception;

public class CommentException extends Exception {
    public CommentException(String postIdDoesNotMatch) {
        super(postIdDoesNotMatch);
    }
}
