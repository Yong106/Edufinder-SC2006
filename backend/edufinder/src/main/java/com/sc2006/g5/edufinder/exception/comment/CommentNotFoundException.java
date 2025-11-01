package com.sc2006.g5.edufinder.exception.comment;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

public class CommentNotFoundException extends ApiException {
    public CommentNotFoundException(Long commentId){
        super("Comment not found with id: %d".formatted(commentId), HttpStatus.NOT_FOUND);
    }
}
