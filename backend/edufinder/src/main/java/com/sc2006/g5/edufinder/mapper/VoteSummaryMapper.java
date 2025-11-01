package com.sc2006.g5.edufinder.mapper;

import org.springframework.stereotype.Component;

import com.sc2006.g5.edufinder.dto.response.VoteSummaryResponse;
import com.sc2006.g5.edufinder.model.comment.Vote;
import com.sc2006.g5.edufinder.model.comment.VoteId;
import com.sc2006.g5.edufinder.model.comment.VoteType;
import com.sc2006.g5.edufinder.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VoteSummaryMapper {

    private final VoteRepository voteRepository;

    public VoteSummaryResponse buildVoteSummary(Long userId, Long commentId) {
        VoteType userVoteType = VoteType.NOVOTE;

        if (userId != null) {
            userVoteType = voteRepository.findById(new VoteId(commentId, userId))
                .map(Vote::getVoteType)
                .orElse(VoteType.NOVOTE);
        }

        Long upvoteCount = voteRepository.countByIdCommentIdAndVoteType(commentId, VoteType.UPVOTE);
        Long downvoteCount = voteRepository.countByIdCommentIdAndVoteType(commentId, VoteType.DOWNVOTE);

        return VoteSummaryResponse.builder()
            .userVoteType(userVoteType)
            .upvoteCount(upvoteCount)
            .downvoteCount(downvoteCount)
            .build();
    }
}

