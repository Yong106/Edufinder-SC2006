package com.sc2006.g5.edufinder.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sc2006.g5.edufinder.dto.request.CreateCommentRequest;
import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.CommentsResponse;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.exception.security.AccessDeniedException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.mapper.CommentMapper;
import com.sc2006.g5.edufinder.model.school.DbSchool;
import com.sc2006.g5.edufinder.model.User;
import com.sc2006.g5.edufinder.model.comment.Comment;
import com.sc2006.g5.edufinder.repository.CommentRepository;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    
    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    DbSchoolRepository schoolRepository;

    @Mock
    CommentMapper commentMapper;

    @InjectMocks
    CommentServiceImpl commentServiceImpl;

    private static final Long EXISTED_USER_ID = 1L;
    private static final String EXISTED_USERNAME = "user";
    private static final Long INVALID_USER_ID = 2L;

    private static final Long EXISTED_SCHOOL_ID = 11L;
    private static final Long INVALID_SCHOOL_ID = 12L;

    private static final Long USER_COMMENT_ID = 21L;
    private static final Long OTHER_COMMENT_ID = 22L;
    private static final Long NEW_COMMENT_ID = 13L;

    private static final String NEW_COMMENT_CONTENT = "new";

    @BeforeEach
    void setup() {        
        lenient().when(schoolRepository.existsById(EXISTED_SCHOOL_ID))
            .thenReturn(true);

        lenient().when(schoolRepository.existsById(INVALID_SCHOOL_ID))
            .thenReturn(false);
    }

    @Nested
    @DisplayName("getCommentsBySchoolId()")
    class GetCommentsBySchoolIdTest{

        @Test
        @DisplayName("should return comments when request valid")
        void shouldReturnCommentsWhenRequestValid(){
            Comment userComment = Comment.builder()
                .id(USER_COMMENT_ID)
                .build();

            Comment otherComment = Comment.builder()
                .id(OTHER_COMMENT_ID)
                .build();
            
            when(commentRepository.findAllBySchoolId(EXISTED_SCHOOL_ID))
                .thenReturn(List.of(userComment, otherComment));

            when(commentMapper.toCommentResponse(argThat(userId ->
                userId.equals(EXISTED_USER_ID)
            ), any())).thenAnswer(invocation -> {
                Comment comment = invocation.getArgument(1);
                return CommentResponse.builder()
                    .id(comment.getId())
                    .build();
            });

            CommentsResponse response = commentServiceImpl.getCommentsBySchoolId(EXISTED_USER_ID, EXISTED_SCHOOL_ID);
            List<CommentResponse> responses = response.getComments();

            assertEquals(2, responses.size());
            assertEquals(USER_COMMENT_ID, responses.get(0).getId());
            assertEquals(OTHER_COMMENT_ID, responses.get(1).getId());

            verify(schoolRepository, times(1)).existsById(any());
            verify(commentMapper, times(2)).toCommentResponse(any(), any());
        }

        @Test
        @DisplayName("should throw when school not found")
        void shouldThrowWhenSchoolNotFound(){
            assertThrowsExactly(SchoolNotFoundException.class, () ->
                commentServiceImpl.getCommentsBySchoolId(EXISTED_USER_ID, INVALID_SCHOOL_ID)
            );

            verify(schoolRepository, times(1)).existsById(any());
            verify(commentMapper, never()).toCommentResponse(any(), any());
        }

    }

    @Nested
    @DisplayName("createComment()")
    class AddCommentTest {
        @BeforeEach
        void setup(){
            User user = User.builder()
                .id(EXISTED_USER_ID)
                .username(EXISTED_USERNAME)
                .build();

            DbSchool school = DbSchool.builder()
                .id(EXISTED_SCHOOL_ID)
                .build();

            lenient().when(userRepository.findById(EXISTED_USER_ID))
                .thenReturn(Optional.of(user));

            lenient().when(schoolRepository.findById(EXISTED_SCHOOL_ID))
                .thenReturn(Optional.of(school));
        }

        @Test
        @DisplayName("should create comment when request valid")
        void shouldCreateCommentWhenRequestValid(){
            when(commentRepository.save(argThat(comment -> 
                comment.getContent().equals(NEW_COMMENT_CONTENT) &&
                comment.getUser().getId().equals(EXISTED_USER_ID) &&
                comment.getSchool().getId().equals(EXISTED_SCHOOL_ID)
            ))).thenAnswer(invocation -> {
                Comment comment = invocation.getArgument(0);
                comment.setId(NEW_COMMENT_ID);
                return comment;
            });

            CommentResponse mapperResponse = CommentResponse.builder()
                .id(NEW_COMMENT_ID)
                .build();

            when(commentMapper.toCommentResponse(
                argThat(userId -> userId.equals(EXISTED_USER_ID)), 
                argThat(comment -> comment.getId().equals(NEW_COMMENT_ID)) 
            )).thenReturn(mapperResponse);

            CreateCommentRequest request = CreateCommentRequest.builder()
                .content(NEW_COMMENT_CONTENT)
                .build();
            
            CommentResponse serviceResponse = commentServiceImpl.createComment(EXISTED_USER_ID, EXISTED_SCHOOL_ID, request);

            assertEquals(mapperResponse, serviceResponse);

            verify(userRepository, times(1)).findById(any());
            verify(schoolRepository, times(1)).findById(any());
            verify(commentRepository, times(1)).save(any());
            verify(commentMapper, times(1)).toCommentResponse(any(), any());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound(){
            when(userRepository.findById(INVALID_USER_ID))
                .thenReturn(Optional.empty());

            CreateCommentRequest request = CreateCommentRequest.builder()
                .content(NEW_COMMENT_CONTENT)
                .build();
            
            assertThrowsExactly(UserNotFoundException.class, () ->
                commentServiceImpl.createComment(INVALID_USER_ID, EXISTED_SCHOOL_ID, request)
            );

            verify(userRepository, times(1)).findById(any());
            verify(schoolRepository, never()).findById(any());
            verify(commentRepository, never()).save(any());
        } 

        @Test
        @DisplayName("should throw when school not found")
        void shouldThrowWhenSchoolNotFound(){
            when(schoolRepository.findById(INVALID_SCHOOL_ID))
                .thenReturn(Optional.empty());

            CreateCommentRequest request = CreateCommentRequest.builder()
                .content(NEW_COMMENT_CONTENT)
                .build();
            
            assertThrowsExactly(SchoolNotFoundException.class, () ->
                commentServiceImpl.createComment(EXISTED_USER_ID, INVALID_SCHOOL_ID, request)
            );

            verify(userRepository, times(1)).findById(any());
            verify(schoolRepository, times(1)).findById(any());
            verify(commentRepository, never()).save(any());
        } 
    }

    @Nested
    @DisplayName("deleteComment()")
    class DeleteCommentTest {

        @BeforeEach
        void setup(){
            Comment comment = Comment.builder()
                .id(USER_COMMENT_ID)
                .build();

            lenient().when(commentRepository.findOneByIdAndUserId(anyLong(), anyLong()))
                .thenAnswer(invocation -> {
                    Long commentId = invocation.getArgument(0);
                    Long userId = invocation.getArgument(1);

                    if(commentId.equals(USER_COMMENT_ID) && userId.equals(EXISTED_USER_ID)){
                        return Optional.of(comment);
                    }

                    return Optional.empty();
                });
        }

        @Test
        @DisplayName("should delete comment when request valid")
        void shouldDeleteCommentWhenRequestValid(){
            doNothing().when(commentRepository).delete(argThat(comment -> 
                comment.getId().equals(USER_COMMENT_ID)
            ));

            commentServiceImpl.deleteComment(EXISTED_USER_ID, USER_COMMENT_ID);

            verify(commentRepository, times(1)).findOneByIdAndUserId(any(), any());
            verify(commentRepository, times(1)).delete(any());
        }

        @Test
        @DisplayName("should throw when user not owner")
        void shouldThrowWhenUserNotOwner(){            
            assertThrowsExactly(AccessDeniedException.class, () ->
                commentServiceImpl.deleteComment(EXISTED_USER_ID, OTHER_COMMENT_ID)
            );

            assertThrowsExactly(AccessDeniedException.class, () ->
                commentServiceImpl.deleteComment(INVALID_USER_ID, USER_COMMENT_ID)
            );

            verify(commentRepository, times(2)).findOneByIdAndUserId(any(), any());
            verify(commentRepository, never()).delete(any());
        } 
    
    }
}
