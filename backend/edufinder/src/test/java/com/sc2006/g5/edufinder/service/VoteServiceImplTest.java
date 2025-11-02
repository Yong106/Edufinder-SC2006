package com.sc2006.g5.edufinder.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.dto.request.SetVoteRequest;
import com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.model.user.User;
import com.sc2006.g5.edufinder.model.comment.Comment;
import com.sc2006.g5.edufinder.model.comment.Vote;
import com.sc2006.g5.edufinder.model.comment.VoteId;
import com.sc2006.g5.edufinder.model.comment.VoteType;
import com.sc2006.g5.edufinder.repository.CommentRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;
import com.sc2006.g5.edufinder.repository.VoteRepository;

@ExtendWith(MockitoExtension.class)
public class VoteServiceImplTest {

    @Mock
    VoteRepository voteRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    VoteServiceImpl voteServiceImpl;

    private static final Long EXISTED_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;

    private static final Long COMMENT_WITH_VOTE_ID = 11L;
    private static final Long COMMENT_WITHOUT_VOTE_ID = 12L;
    private static final Long INVALID_COMMENT_ID = 13L;

    private static final VoteType OLD_VOTE_TYPE = VoteType.NOVOTE;
    private static final VoteType NEW_VOTE_TYPE = VoteType.UPVOTE;
    
    @Nested
    @DisplayName("setVote()")
    class SetVoteTest{

        @BeforeEach
        void setup(){
            User user = User.builder()
                .id(EXISTED_USER_ID)
                .build();

            Comment comment_with_vote = Comment.builder()
                .id(COMMENT_WITH_VOTE_ID)
                .build();

            Comment comment_without_vote = Comment.builder()
                .id(COMMENT_WITHOUT_VOTE_ID)
                .build();

            Vote vote = Vote.builder()
                .id(new VoteId(COMMENT_WITH_VOTE_ID, EXISTED_USER_ID))
                .comment(comment_with_vote)
                .user(user)
                .voteType(OLD_VOTE_TYPE)
                .build();

            lenient().when(userRepository.findById(EXISTED_USER_ID))
                .thenReturn(Optional.of(user));

            lenient().when(commentRepository.findById(COMMENT_WITHOUT_VOTE_ID))
                .thenReturn(Optional.of(comment_without_vote));

            lenient().when(voteRepository.findById(any(VoteId.class)))
                .thenAnswer(invocation -> {
                    VoteId voteId = invocation.getArgument(0);

                    if(voteId.getUserId().equals(EXISTED_USER_ID) && voteId.getCommentId().equals(COMMENT_WITH_VOTE_ID)){
                        return Optional.of(vote);
                    }

                    return Optional.empty();
                });
        }

        @Test
        @DisplayName("should update vote when vote existed")
        void shouldUpdateVoteWhenVoteExisted(){
            when(voteRepository.save(argThat(vote -> 
                vote.getId().getUserId().equals(EXISTED_USER_ID) &&
                vote.getId().getCommentId().equals(COMMENT_WITH_VOTE_ID) &&
                vote.getUser().getId().equals(EXISTED_USER_ID) &&
                vote.getComment().getId().equals(COMMENT_WITH_VOTE_ID) &&
                vote.getVoteType().equals(NEW_VOTE_TYPE)
            ))).thenAnswer(invocation -> invocation.getArgument(0));

            SetVoteRequest request = SetVoteRequest.builder()
                .voteType(NEW_VOTE_TYPE)
                .build();

            voteServiceImpl.setVote(EXISTED_USER_ID, COMMENT_WITH_VOTE_ID, request);

            verify(voteRepository, times(1)).findById(any());
            verify(userRepository, never()).findById(any());
            verify(commentRepository, never()).findById(any());
            verify(voteRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("should create vote when vote not existed")
        void shouldCreateVoteWhenVoteNotExisted(){
            when(voteRepository.save(argThat(vote -> 
                vote.getId().getUserId().equals(EXISTED_USER_ID) &&
                vote.getId().getCommentId().equals(COMMENT_WITHOUT_VOTE_ID) &&
                vote.getUser().getId().equals(EXISTED_USER_ID) &&
                vote.getComment().getId().equals(COMMENT_WITHOUT_VOTE_ID) &&
                vote.getVoteType().equals(NEW_VOTE_TYPE)
            ))).thenAnswer(invocation -> invocation.getArgument(0));

            SetVoteRequest request = SetVoteRequest.builder()
                .voteType(NEW_VOTE_TYPE)
                .build();

            voteServiceImpl.setVote(EXISTED_USER_ID, COMMENT_WITHOUT_VOTE_ID, request);

            verify(voteRepository, times(1)).findById(any());
            verify(userRepository, times(1)).findById(any());
            verify(commentRepository, times(1)).findById(any());
            verify(voteRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound(){
            when(userRepository.findById(INVALID_USER_ID))
                .thenReturn(Optional.empty());

            SetVoteRequest request = SetVoteRequest.builder()
                .voteType(NEW_VOTE_TYPE)
                .build();
            
            assertThrows(UserNotFoundException.class, () ->
                voteServiceImpl.setVote(INVALID_USER_ID, COMMENT_WITH_VOTE_ID, request)
            );

            verify(voteRepository, times(1)).findById(any());
            verify(userRepository, times(1)).findById(any());
            verify(commentRepository, never()).findById(any());
            verify(voteRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw when comment not found")
        void shouldThrowWhenCommentNotFound(){
            when(commentRepository.findById(INVALID_COMMENT_ID))
                .thenReturn(Optional.empty());

            SetVoteRequest request = SetVoteRequest.builder()
                .voteType(NEW_VOTE_TYPE)
                .build();
            
            assertThrows(CommentNotFoundException.class, () ->
                voteServiceImpl.setVote(EXISTED_USER_ID, INVALID_COMMENT_ID, request)
            );

            verify(voteRepository, times(1)).findById(any());
            verify(userRepository, times(1)).findById(any());
            verify(commentRepository, times(1)).findById(any());
            verify(voteRepository, never()).save(any());
        }
    }

}
