package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;
import com.sc2006.g5.edufinder.dto.response.ReplyResponse;

/**
 * A service responsible for handling reply-related operations.
 * <p>
 * Provides methods for creating comment and deleting reply
 */
public interface ReplyService {

    /**
     * Create a reply by a user for a comment.
     *
     * @param userId id of the user
     * @param commentId id of the comment
     * @param createReplyRequest a request object contain data needed to create reply
     *
     * @return a response object containing the created reply
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with {@code userId} is not found
     * @throws com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException if comment with {@code commentId} is not found
     *
     * @see CreateReplyRequest
     * @see ReplyResponse
     */
    ReplyResponse createReply(Long userId, Long commentId, CreateReplyRequest createReplyRequest);

    /**
     * Delete a reply.
     * @param userId id of the logged-in user
     * @param replyId id of the reply to be deleted
     *
     * @throws com.sc2006.g5.edufinder.exception.security.AccessDeniedException if reply not found or user have insufficient permission
     */
    void deleteReply(Long userId, Long replyId);
}
