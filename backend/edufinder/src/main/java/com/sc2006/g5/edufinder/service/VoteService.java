package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.SetVoteRequest;

/**
 * A service responsible for handling vote-related operations.
 * <p>
 * Provides methods for setting vote.
 */
public interface VoteService {

    /**
     * Set vote by a user for a comment. If vote doesn't exist, new vote is created. Else, vote is edited.
     *
     * @param userId id of the user
     * @param commentId id of the comment
     * @param setVoteRequest request object containing data needed to set vote.
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with`userId` is not found
     * @throws com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException if comment with `commentId` is not found
     */
    void setVote(Long userId, Long commentId, SetVoteRequest setVoteRequest);
}
