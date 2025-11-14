package com.sc2006.g5.edufinder.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the response payload for list of comments returned by the API..
 */
@Data
@Builder
public class CommentsResponse {
    private List<CommentResponse> comments;
}
