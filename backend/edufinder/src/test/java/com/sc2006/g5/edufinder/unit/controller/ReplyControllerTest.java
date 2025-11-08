package com.sc2006.g5.edufinder.unit.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.controller.ReplyController;
import com.sc2006.g5.edufinder.dto.response.ReplyResponse;
import com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException;
import com.sc2006.g5.edufinder.exception.security.AccessDeniedException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.security.AuthFilter;
import com.sc2006.g5.edufinder.service.ReplyService;
import com.sc2006.g5.edufinder.unit.setup.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReplyController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthFilter authFilter;

    @MockitoBean
    private ReplyService replyService;

    private static final long EXISTED_USER_ID = 1L;
    private static final long INVALID_USER_ID = 2L;

    private static final long EXISTED_COMMENT_ID = 11L;
    private static final long INVALID_COMMENT_ID = 12L;

    private static final long USER_REPLY_ID = 21L;
    private static final long OTHER_REPLY_ID = 22L;

    private static final String REPLY_CONTENT = "reply content";

    @Nested
    @DisplayName("POST /api/comments/{commentId}/replies")
    class PostReplies {

        private MockHttpServletRequestBuilder mockRawRequest(Long commentId, String content) {
            return post("/api/comments/%d/replies".formatted(commentId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(Long commentId, String content) {
            return mockRawRequest(commentId, """
                    {"content": "%s"}
                """.formatted(content));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 200 with reply response when request valid")
        void shouldReturn200WithReplyResponseWhenRequestValid() throws Exception {

            ReplyResponse response = ReplyResponse.builder()
                .id(USER_REPLY_ID)
                .content(REPLY_CONTENT)
                .build();

            when(replyService.createReply(
                eq(EXISTED_USER_ID),
                eq(EXISTED_COMMENT_ID),
                argThat(request -> request.getContent().equals(REPLY_CONTENT)))
            ).thenReturn(response);

            mockMvc.perform(mockRequest(EXISTED_COMMENT_ID, REPLY_CONTENT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_REPLY_ID))
                .andExpect(jsonPath("$.content").value(REPLY_CONTENT));

            verify(replyService, times(1)).createReply(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(post("/api/comments/abc/replies"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_COMMENT_ID, "{)"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_COMMENT_ID, """
                {"con": "%s"}
            """)).andExpect(status().isBadRequest());

            verify(replyService, never()).createReply(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id = INVALID_USER_ID)
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {

            when(replyService.createReply(
                eq(INVALID_USER_ID),
                eq(EXISTED_COMMENT_ID),
                argThat(request -> request.getContent().equals(REPLY_CONTENT)))
            ).thenAnswer(invocation -> {
                throw new UserNotFoundException(INVALID_USER_ID);
            });

            mockMvc.perform(mockRequest(EXISTED_COMMENT_ID, REPLY_CONTENT))
                .andExpect(status().isNotFound());

            verify(replyService, times(1)).createReply(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 404 when comment not found")
        void shouldReturn404WhenCommentNotFound() throws Exception {

            when(replyService.createReply(
                eq(EXISTED_USER_ID),
                eq(INVALID_COMMENT_ID),
                argThat(request -> request.getContent().equals(REPLY_CONTENT)))
            ).thenAnswer(invocation -> {
                throw new CommentNotFoundException(INVALID_COMMENT_ID);
            });

            mockMvc.perform(mockRequest(INVALID_COMMENT_ID, REPLY_CONTENT))
                .andExpect(status().isNotFound());

            verify(replyService, times(1)).createReply(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("DELETE /api/replies/{replyId}")
    class DeleteReplyTest {

        private MockHttpServletRequestBuilder mockRequest(Long replyId) {
            return delete("/api/replies/%d".formatted(replyId));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 204 when request valid")
        void shouldReturn204WhenRequestValid() throws Exception {
            mockMvc.perform(mockRequest(USER_REPLY_ID))
                .andExpect(status().isNoContent());

            verify(replyService, times(1)).deleteReply(EXISTED_USER_ID, USER_REPLY_ID);
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(delete("/api/replies/abc"))
                .andExpect(status().isBadRequest());

            verify(replyService, never()).deleteReply(any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 404 when access denied")
        void shouldReturn404WhenAccessDenied() throws Exception {
            doThrow(new AccessDeniedException()).when(replyService)
                .deleteReply(EXISTED_USER_ID, OTHER_REPLY_ID);

            mockMvc.perform(mockRequest(OTHER_REPLY_ID))
                .andExpect(status().isNotFound());

            verify(replyService, times(1)).deleteReply(any(), any());
        }

    }

}
