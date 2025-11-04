package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.config.SecurityConfig;
import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.CommentsResponse;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.exception.security.AccessDeniedException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.security.AuthFilter;
import com.sc2006.g5.edufinder.service.CommentService;
import com.sc2006.g5.edufinder.setup.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        AuthFilter.class,
        SecurityConfig.class
    })
})
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    private static final Long EXISTED_SCHOOL_ID = 1L;
    private static final Long INVALID_SCHOOL_ID = 2L;

    private static final long EXISTED_USER_ID = 11L;
    private static final long INVALID_USER_ID = 12L;

    private static final Long USER_COMMENT_ID = 21L;
    private static final Long OTHER_COMMENT_ID = 22L;

    private static final String COMMENT_CONTENT = "comment_content";

    @Nested
    @DisplayName("GET /api/schools/{schoolId}/comments")
    class GetCommentsBySchoolIdTest {

        private MockHttpServletRequestBuilder mockRequest(Long schoolId) {
            return get("/api/schools/%d/comments".formatted(schoolId));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 200 with comments response when authenticated")
        void shouldReturn200WithCommentsWhenAuthenticated() throws Exception {
            CommentResponse commentResponse1 = CommentResponse.builder()
                .id(USER_COMMENT_ID)
                .build();

            CommentResponse commentResponse2 = CommentResponse.builder()
                .id(OTHER_COMMENT_ID)
                .build();

            CommentsResponse commentsResponse = CommentsResponse.builder()
                .comments(List.of(commentResponse1, commentResponse2))
                .build();

            when(commentService.getCommentsBySchoolId(EXISTED_USER_ID, EXISTED_SCHOOL_ID))
                .thenReturn(commentsResponse);

            mockMvc.perform(mockRequest(EXISTED_SCHOOL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].id").value(USER_COMMENT_ID))
                .andExpect(jsonPath("$.comments[1].id").value(OTHER_COMMENT_ID));

            verify(commentService, times(1)).getCommentsBySchoolId(any(), any());
        }

        @Test
        @DisplayName("should return 200 with comments response when anonymous")
        void shouldReturn200WithCommentsWhenAnonymous() throws Exception {
            CommentResponse commentResponse1 = CommentResponse.builder()
                .id(USER_COMMENT_ID)
                .build();

            CommentResponse commentResponse2 = CommentResponse.builder()
                .id(OTHER_COMMENT_ID)
                .build();

            CommentsResponse commentsResponse = CommentsResponse.builder()
                .comments(List.of(commentResponse1, commentResponse2))
                .build();

            when(commentService.getCommentsBySchoolId(null, EXISTED_SCHOOL_ID))
                .thenReturn(commentsResponse);

            mockMvc.perform(mockRequest(EXISTED_SCHOOL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].id").value(USER_COMMENT_ID))
                .andExpect(jsonPath("$.comments[1].id").value(OTHER_COMMENT_ID));

            verify(commentService, times(1)).getCommentsBySchoolId(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(get("/api/schools/abc/comments"))
                .andExpect(status().isBadRequest());

            verify(commentService, never()).getCommentsBySchoolId(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 404 when school not found")
        void shouldReturn404WhenSchoolNotFound() throws Exception {
            when(commentService.getCommentsBySchoolId(EXISTED_USER_ID, INVALID_SCHOOL_ID))
                .thenAnswer(invocation -> {
                    throw new SchoolNotFoundException(INVALID_SCHOOL_ID);
                });

            mockMvc.perform(mockRequest(INVALID_SCHOOL_ID))
                .andExpect(status().isNotFound());

            verify(commentService, times(1)).getCommentsBySchoolId(any(), any());
        }
    }

    @Nested
    @DisplayName("POST /api/schools/{schoolId}/comments")
    class CreateCommentsTest {

        private MockHttpServletRequestBuilder mockRawRequest(Long schoolId, String content) {
            return post("/api/schools/%d/comments".formatted(schoolId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(Long schoolId, String content) {
            return post("/api/schools/%d/comments".formatted(schoolId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"content": "%s"}
                """.formatted(content));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        void shouldReturn200WithCommentResponseWhenRequestValid() throws Exception {
            CommentResponse response = CommentResponse.builder()
                .id(USER_COMMENT_ID)
                .content(COMMENT_CONTENT)
                .build();

            when(commentService.createComment(
                eq(EXISTED_USER_ID),
                eq(EXISTED_SCHOOL_ID),
                argThat(request -> request.getContent().equals(COMMENT_CONTENT)))
            ).thenReturn(response);

            mockMvc.perform(mockRequest(EXISTED_SCHOOL_ID, COMMENT_CONTENT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_COMMENT_ID))
                .andExpect(jsonPath("$.content").value(COMMENT_CONTENT));

            verify(commentService, times(1)).createComment(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(post("/api/schools/abc/comments"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_SCHOOL_ID, "{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_SCHOOL_ID,"""
                {"con": "1"}
            """)).andExpect(status().isBadRequest());

           verify(commentService, never()).createComment(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id = INVALID_USER_ID)
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {
            when(commentService.createComment(
                eq(INVALID_USER_ID),
                eq(EXISTED_SCHOOL_ID),
                argThat(request -> request.getContent().equals(COMMENT_CONTENT)))
            ).thenAnswer(invocation -> {
                throw new UserNotFoundException(INVALID_USER_ID);
            });

            mockMvc.perform(mockRequest(EXISTED_SCHOOL_ID, COMMENT_CONTENT))
                .andExpect(status().isNotFound());

            verify(commentService, times(1)).createComment(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 404 when school not found")
        void shouldReturn404WhenSchoolNotFound() throws Exception {
            when(commentService.createComment(
                eq(EXISTED_USER_ID),
                eq(INVALID_SCHOOL_ID),
                argThat(request -> request.getContent().equals(COMMENT_CONTENT)))
            ).thenAnswer(invocation -> {
                throw new UserNotFoundException(INVALID_USER_ID);
            });

            mockMvc.perform(mockRequest(INVALID_SCHOOL_ID, COMMENT_CONTENT))
                .andExpect(status().isNotFound());

            verify(commentService, times(1)).createComment(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /api/comments/{commentId}")
    class DeleteCommentTest {

        private MockHttpServletRequestBuilder mockRequest(Long commentId) {
            return delete("/api/comments/%d".formatted(commentId));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 204 when request valid")
        void shouldReturn204WhenRequestValid() throws Exception {
            mockMvc.perform(mockRequest(USER_COMMENT_ID))
                .andExpect(status().isNoContent());

            verify(commentService, times(1)).deleteComment(EXISTED_USER_ID, USER_COMMENT_ID);
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(delete("/api/comments/abc"))
                .andExpect(status().isBadRequest());

            verify(commentService, never()).deleteComment(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 404 when access denied")
        void shouldReturn404WhenAccessDenied() throws Exception {
            doThrow(new AccessDeniedException()).when(commentService)
                .deleteComment(EXISTED_USER_ID, OTHER_COMMENT_ID);

            mockMvc.perform(mockRequest(OTHER_COMMENT_ID))
                .andExpect(status().isNotFound());

            verify(commentService, times(1)).deleteComment(any(), any());
        }
    }
}
