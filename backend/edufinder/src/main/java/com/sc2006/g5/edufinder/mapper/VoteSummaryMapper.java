package com.sc2006.g5.edufinder.mapper;

import org.springframework.stereotype.Component;

import com.sc2006.g5.edufinder.dto.response.VoteSummaryResponse;
import com.sc2006.g5.edufinder.model.comment.Vote;
import com.sc2006.g5.edufinder.model.comment.VoteId;
import com.sc2006.g5.edufinder.model.comment.VoteType;
import com.sc2006.g5.edufinder.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

/**
 * Mapper for vote-related model.
 * <p>
 * Provides method to map to {@link VoteSummaryResponse} DTOs.
 */
@Component
@RequiredArgsConstructor
public class VoteSummaryMapper {

    private final VoteRepository voteRepository;

    /**
     * Constructs a {@link VoteSummaryResponse} based on user and comment.
     *
     * @param userId the id of the user, null if user is anonymous
     * @param commentId the id of the comment
     *
     * @return the constructed {@code VoteSummaryResponse}
     *
     * @see VoteSummaryResponse
     */
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

