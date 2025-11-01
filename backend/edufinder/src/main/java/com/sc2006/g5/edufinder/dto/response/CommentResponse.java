package com.sc2006.g5.edufinder.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

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
