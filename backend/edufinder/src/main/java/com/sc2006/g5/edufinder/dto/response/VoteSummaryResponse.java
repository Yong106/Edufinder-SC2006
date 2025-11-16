package com.sc2006.g5.edufinder.dto.response;

import com.sc2006.g5.edufinder.model.comment.VoteType;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the response payload for vote information related to comment by the API.
 * <p>
 * Contains the vote type of current user, total upvote count, and total downvote count.
 */
@Data
@Builder
public class VoteSummaryResponse {
    private VoteType userVoteType;
    private Long upvoteCount;
    private Long downvoteCount;
}
