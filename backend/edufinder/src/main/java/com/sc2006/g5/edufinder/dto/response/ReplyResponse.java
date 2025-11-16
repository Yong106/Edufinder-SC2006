package com.sc2006.g5.edufinder.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the response payload for a reply returned by the API.
 */
@Data
@Builder
public class ReplyResponse {
    
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    
}
