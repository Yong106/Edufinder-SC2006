package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.CreateCommentRequest;
import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.CommentsResponse;

/**
 * A service responsible for handling comment-related operations.
 * <p>
 * Provides methods for getting comments by school, creating comment, and deleting comment
 */
public interface CommentService {

    /**
     * Get all comments of a school, including related replies and vote information.
     *
     * @param userId id of the logged-in user. Set to null if user is anonymous
     * @param schoolId id of the school
     * @return a response object containing all comments of the school and related data
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with `userId` is not found
     * @throws com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException if school with `schoolId` is not found
     *
     * @see CommentsResponse
     */
    CommentsResponse getCommentsBySchoolId(Long userId, Long schoolId);

    /**
     * Create a comment by a user for a school.
     *
     * @param userId id of the user
     * @param schoolId id of the school
     * @param createCommentRequest a request object contain data needed to create comment
     *
     * @return a response object containing the created comment and related data
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with `userId` is not found
     * @throws com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException if school with `schoolId` is not found
     *
     * @see CreateCommentRequest
     * @see CommentResponse
     */
    CommentResponse createComment(Long userId, Long schoolId, CreateCommentRequest createCommentRequest);

    /**
     * Delete a comment.
     * @param userId id of the logged-in user
     * @param commentId id of the comment to be deleted
     *
     * @throws com.sc2006.g5.edufinder.exception.security.AccessDeniedException if comment not found or user have insufficient permission
     */
    void deleteComment(Long userId, Long commentId);
}