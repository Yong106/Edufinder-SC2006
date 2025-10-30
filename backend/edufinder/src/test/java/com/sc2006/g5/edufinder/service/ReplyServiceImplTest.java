package com.sc2006.g5.edufinder.service;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
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

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;
import com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException;
import com.sc2006.g5.edufinder.exception.security.AccessDeniedException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.model.User;
import com.sc2006.g5.edufinder.model.comment.Comment;
import com.sc2006.g5.edufinder.model.comment.Reply;
import com.sc2006.g5.edufinder.repository.CommentRepository;
import com.sc2006.g5.edufinder.repository.ReplyRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ReplyServiceImplTest {

    @Mock
    ReplyRepository replyRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    ReplyServiceImpl replyServiceImpl;

    private static final Long EXISTED_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 2L;

    private static final Long EXISTED_COMMENT_ID = 11L;
    private static final Long INVALID_COMMENT_ID = 12L;

    private static final Long USER_REPLY_ID = 21L;
    private static final Long OTHER_REPLY_ID = 22L;

    private static final String NEW_REPLY_CONTENT = "new";

    @Nested
    @DisplayName("createReply()")
    class CreateReplyTest {

        @BeforeEach
        void setup(){
            User user = User.builder()
                .id(EXISTED_USER_ID)
                .build();

            Comment comment = Comment.builder()
                .id(EXISTED_COMMENT_ID)
                .build();

            lenient().when(userRepository.findById(EXISTED_USER_ID))
                .thenReturn(Optional.of(user));

            lenient().when(commentRepository.findById(EXISTED_COMMENT_ID))
                .thenReturn(Optional.of(comment));
        }
        
        @Test
        @DisplayName("should create reply when request valid")
        void shouldCreateReplyWhenRequestValid(){
            CreateReplyRequest request = CreateReplyRequest.builder()
                .commentId(EXISTED_COMMENT_ID)
                .content(NEW_REPLY_CONTENT)
                .build();

            when(replyRepository.save(argThat(reply -> 
                reply.getContent().equals(NEW_REPLY_CONTENT) && 
                reply.getUser().getId().equals(EXISTED_USER_ID) &&
                reply.getComment().getId().equals(EXISTED_COMMENT_ID)
            ))).thenAnswer(invocation -> invocation.getArgument(0));

            replyServiceImpl.createReply(EXISTED_USER_ID, request);

            verify(userRepository, times(1)).findById(any());
            verify(commentRepository, times(1)).findById(any());
            verify(replyRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound(){
            when(userRepository.findById(INVALID_USER_ID))
                .thenReturn(Optional.empty());

            CreateReplyRequest request = CreateReplyRequest.builder()
                .commentId(EXISTED_COMMENT_ID)
                .content(NEW_REPLY_CONTENT)
                .build();
            
            assertThrowsExactly(UserNotFoundException.class, () -> {
                replyServiceImpl.createReply(INVALID_USER_ID, request);
            });

            verify(userRepository, times(1)).findById(any());
            verify(commentRepository, never()).findById(any());
            verify(replyRepository, never()).save(any());
        } 

        @Test
        @DisplayName("should throw when comment not found")
        void shouldThrowWhenCommentNotFound(){
            when(commentRepository.findById(INVALID_COMMENT_ID))
                .thenReturn(Optional.empty());

            CreateReplyRequest request = CreateReplyRequest.builder()
                .commentId(INVALID_COMMENT_ID)
                .content(NEW_REPLY_CONTENT)
                .build();
            
            assertThrowsExactly(CommentNotFoundException.class, () -> {
                replyServiceImpl.createReply(EXISTED_USER_ID, request);
            });

            verify(userRepository, times(1)).findById(any());
            verify(commentRepository, times(1)).findById(any());
            verify(replyRepository, never()).save(any());
        } 
    }

    @Nested
    @DisplayName("deleteReply()")
    class DeleteReplyTest {

        @BeforeEach
        void setup(){
            Reply reply = Reply.builder()
                .id(USER_REPLY_ID)
                .build();

            lenient().when(replyRepository.findOneByIdAndUserId(anyLong(), anyLong()))
                .thenAnswer(invocation -> {
                    Long replyId = invocation.getArgument(0);
                    Long userId = invocation.getArgument(1);

                    if(replyId.equals(USER_REPLY_ID) && userId.equals(EXISTED_USER_ID)){
                        return Optional.of(reply);
                    }

                    return Optional.empty();
                });
        }

        @Test
        @DisplayName("should delete reply when request valid")
        void shouldDeleteReplyWhenRequestValid(){
            doNothing().when(replyRepository).delete(argThat(reply -> 
                reply.getId().equals(USER_REPLY_ID)
            ));

            replyServiceImpl.deleteReply(EXISTED_USER_ID, USER_REPLY_ID);

            verify(replyRepository, times(1)).findOneByIdAndUserId(any(), any());
            verify(replyRepository, times(1)).delete(any());
        }

        @Test
        @DisplayName("should throw when user not owner")
        void shouldThrowWhenUserNotOwner(){            
            assertThrowsExactly(AccessDeniedException.class, () -> {
                replyServiceImpl.deleteReply(EXISTED_USER_ID, OTHER_REPLY_ID);
            });

            assertThrowsExactly(AccessDeniedException.class, () -> {
                replyServiceImpl.deleteReply(INVALID_USER_ID, USER_REPLY_ID);
            });

            verify(replyRepository, times(2)).findOneByIdAndUserId(any(), any());
            verify(replyRepository, never()).delete(any());
        } 

    }
}
