package com.sc2006.g5.edufinder.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the response payload for a comment returned by the API.
 * <p>
 * Contains the comment's basic information, its replies, and aggregated vote data.
 */
@Data
@Builder
public class CommentResponse {
    
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private List<ReplyResponse> replies;
    private VoteSummaryResponse voteSummary;
}
