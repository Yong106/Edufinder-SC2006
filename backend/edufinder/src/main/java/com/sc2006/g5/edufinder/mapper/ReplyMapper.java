package com.sc2006.g5.edufinder.mapper;

import org.springframework.stereotype.Component;

import com.sc2006.g5.edufinder.dto.response.ReplyResponse;
import com.sc2006.g5.edufinder.model.comment.Reply;

/**
 * Mapper for reply-related model.
 * <p>
 * Provides method to map {@link Reply} to {@link ReplyResponse} DTOs.
 */
@Component
public class ReplyMapper {

    /**
     * Converts a {@link Reply} entity into a {@link ReplyResponse}.
     *
     * @param reply the reply to map
     *
     * @return the mapped {@code CommentResponse}
     *
     * @see Reply
     * @see ReplyResponse
     */
    public ReplyResponse toReplyResponse(Reply reply) {
        return ReplyResponse.builder()
            .id(reply.getId())
            .content(reply.getContent())
            .username(reply.getUser().getUsername())
            .createdAt(reply.getCreatedAt())
            .build();
    }
}
