package com.sc2006.g5.edufinder.unit.controller;

import com.sc2006.g5.edufinder.config.GlobalExceptionHandler;
import com.sc2006.g5.edufinder.config.SecurityConfig;
import com.sc2006.g5.edufinder.controller.VoteController;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.model.comment.VoteType;
import com.sc2006.g5.edufinder.security.AuthFilter;
import com.sc2006.g5.edufinder.service.VoteService;
import com.sc2006.g5.edufinder.unit.setup.WithMockCustomUser;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VoteController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        AuthFilter.class,
        SecurityConfig.class
    })
})
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VoteService voteService;

    private static final long EXISTED_USER_ID = 1L;
    private static final long INVALID_USER_ID = 2L;

    private static final long EXISTED_COMMENT_ID = 11L;
    private static final long INVALID_COMMENT_ID = 12L;

    @Nested
    @DisplayName("PUT /api/comments/{commentId}/votes")
    class SetVoteTest {

        private MockHttpServletRequestBuilder mockRawRequest(long commentId, String content) {
            return put("/api/comments/%d/votes".formatted(commentId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        }

        private MockHttpServletRequestBuilder mockRequest(long commentId, VoteType voteType) {
            return mockRawRequest(commentId, """
                {"voteType": "%s"}
            """.formatted(voteType.toString()));
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 204 when request valid")
        void shouldReturn204WhenRequestValid() throws Exception {
            VoteType voteType = VoteType.UPVOTE;

            mockMvc.perform(mockRequest(EXISTED_COMMENT_ID, voteType))
                .andExpect(status().isNoContent());

            verify(voteService, times(1)).setVote(
                eq(EXISTED_USER_ID),
                eq(EXISTED_COMMENT_ID),
                argThat(vote -> vote.getVoteType().equals(voteType))
            );
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 400 when request malformed")
        void shouldReturn400WhenRequestMalformed() throws Exception {
            mockMvc.perform(put("/api/comments/abc/votes"))
                .andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_COMMENT_ID, """
                {"vote": "UPVOTE"}
            """)).andExpect(status().isBadRequest());

            mockMvc.perform(mockRawRequest(EXISTED_COMMENT_ID, """
                {"voteType": "UP"}
            """)).andExpect(status().isBadRequest());

            verify(voteService, never()).setVote(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id =INVALID_USER_ID)
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenUserNotFound() throws Exception {
            VoteType voteType = VoteType.DOWNVOTE;

            doThrow(new UserNotFoundException(INVALID_USER_ID)).when(voteService)
                .setVote(
                    eq(INVALID_USER_ID),
                    eq(EXISTED_COMMENT_ID),
                    argThat(request -> request.getVoteType().equals(voteType))
                );

            mockMvc.perform(mockRequest(EXISTED_COMMENT_ID, voteType))
                .andExpect(status().isNotFound());

            verify(voteService, times(1)).setVote(any(), any(), any());
        }

        @Test
        @WithMockCustomUser(id = EXISTED_USER_ID)
        @DisplayName("should return 404 when comment not found")
        void shouldReturn404WhenCommentNotFound() throws Exception {
            VoteType voteType = VoteType.NOVOTE;

            doThrow(new UserNotFoundException(EXISTED_COMMENT_ID)).when(voteService)
                .setVote(
                    eq(EXISTED_USER_ID),
                    eq(INVALID_COMMENT_ID),
                    argThat(request -> request.getVoteType().equals(voteType))
                );

            mockMvc.perform(mockRequest(INVALID_COMMENT_ID, voteType))
                .andExpect(status().isNotFound());

            verify(voteService, times(1)).setVote(any(), any(), any());
        }
    }
}
