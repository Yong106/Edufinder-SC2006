package com.sc2006.g5.edufinder.exception.comment;

import org.springframework.http.HttpStatus;

import com.sc2006.g5.edufinder.exception.common.ApiException;

/**
 * Thrown when an operation references a comment that does not exist.
 * <p>
 * Returns HTTP 404 Not Found.
 */
public class CommentNotFoundException extends ApiException {

    /**
     * Creates a new exception indicating that the comment with the specified ID could not be found.
     *
     * @param commentId the ID of the missing comment
     */
    public CommentNotFoundException(Long commentId){
        super("Comment not found with id: %d".formatted(commentId), HttpStatus.NOT_FOUND);
    }
}
