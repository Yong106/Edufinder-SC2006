package com.sc2006.g5.edufinder.dto.response;

import com.sc2006.g5.edufinder.model.comment.VoteType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteSummaryResponse {
    private VoteType userVoteType;
    private Long upvoteCount;
    private Long downvoteCount;
}
