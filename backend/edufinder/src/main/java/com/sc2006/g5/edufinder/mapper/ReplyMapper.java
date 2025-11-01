package com.sc2006.g5.edufinder.mapper;

import org.springframework.stereotype.Component;

import com.sc2006.g5.edufinder.dto.response.ReplyResponse;
import com.sc2006.g5.edufinder.model.comment.Reply;

@Component
public class ReplyMapper {
    public ReplyResponse toReplyResponse(Reply reply) {
        return ReplyResponse.builder()
            .id(reply.getId())
            .content(reply.getContent())
            .username(reply.getUser().getUsername())
            .createdAt(reply.getCreatedAt())
            .build();
    }
}
