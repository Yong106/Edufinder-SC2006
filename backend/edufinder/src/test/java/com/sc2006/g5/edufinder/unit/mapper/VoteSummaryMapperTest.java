package com.sc2006.g5.edufinder.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.sc2006.g5.edufinder.mapper.VoteSummaryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.dto.response.VoteSummaryResponse;
import com.sc2006.g5.edufinder.model.comment.Vote;
import com.sc2006.g5.edufinder.model.comment.VoteId;
import com.sc2006.g5.edufinder.model.comment.VoteType;
import com.sc2006.g5.edufinder.repository.VoteRepository;

@ExtendWith(MockitoExtension.class)
public class VoteSummaryMapperTest {

    @Mock
    VoteRepository voteRepository;

    @InjectMocks
    VoteSummaryMapper voteSummaryMapper;

    private static final Long VOTED_USER_ID = 1L;
    private static final Long NOT_VOTED_USER_ID = 2L;

    private static final Long COMMENT_ID = 11L;

    private static final Long UPVOTE_COUNT = 21L;
    private static final Long DOWNVOTE_COUNT = 22L;

    private static final VoteType USER_VOTE_TYPE = VoteType.UPVOTE;
    
    @Nested
    @DisplayName("buildVoteSummary()")
    class BuildVoteSummaryTest{
        
        @BeforeEach
        void setup(){
            Vote vote = Vote.builder()
                .voteType(USER_VOTE_TYPE)
                .build();

            lenient().when(voteRepository.findById(any(VoteId.class)))
                .thenAnswer(invocation -> {
                    VoteId voteId = invocation.getArgument(0);

                    if(voteId.getUserId().equals(VOTED_USER_ID) && voteId.getCommentId().equals(COMMENT_ID)){
                        return Optional.of(vote);
                    }

                    return Optional.empty();
                });

            lenient().when(voteRepository.countByIdCommentIdAndVoteType(COMMENT_ID, VoteType.UPVOTE))
                .thenReturn(UPVOTE_COUNT);

            lenient().when(voteRepository.countByIdCommentIdAndVoteType(COMMENT_ID, VoteType.DOWNVOTE))
                .thenReturn(DOWNVOTE_COUNT);

        }

        @Test
        @DisplayName("should return user vote type when user voted")
        void shouldReturnUserVoteTypeWhenUserVoted(){
            VoteSummaryResponse response = voteSummaryMapper.buildVoteSummary(VOTED_USER_ID, COMMENT_ID);

            assertEquals(USER_VOTE_TYPE, response.getUserVoteType());
            assertEquals(UPVOTE_COUNT, response.getUpvoteCount());
            assertEquals(DOWNVOTE_COUNT, response.getDownvoteCount());

            verify(voteRepository, times(1)).findById(any());
            verify(voteRepository, times(2)).countByIdCommentIdAndVoteType(any(), any());
        }

        @Test
        @DisplayName("should return no vote when user not voted")
        void shouldReturnNoVoteWhenUserNotVoted(){
            VoteSummaryResponse response = voteSummaryMapper.buildVoteSummary(NOT_VOTED_USER_ID, COMMENT_ID);

            assertEquals(VoteType.NOVOTE, response.getUserVoteType());
            assertEquals(UPVOTE_COUNT, response.getUpvoteCount());
            assertEquals(DOWNVOTE_COUNT, response.getDownvoteCount());

            verify(voteRepository, times(1)).findById(any());
            verify(voteRepository, times(2)).countByIdCommentIdAndVoteType(any(), any());
        }

        @Test
        @DisplayName("should return no vote when anonymous")
        void shouldReturnNoVoteWhenAnonymous(){
            VoteSummaryResponse response = voteSummaryMapper.buildVoteSummary(null, COMMENT_ID);

            assertEquals(VoteType.NOVOTE, response.getUserVoteType());
            assertEquals(UPVOTE_COUNT, response.getUpvoteCount());
            assertEquals(DOWNVOTE_COUNT, response.getDownvoteCount());

            verify(voteRepository, never()).findById(any());
            verify(voteRepository, times(2)).countByIdCommentIdAndVoteType(any(), any());
        }

    }
}
