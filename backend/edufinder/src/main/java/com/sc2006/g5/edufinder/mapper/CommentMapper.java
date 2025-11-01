package com.sc2006.g5.edufinder.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.ReplyResponse;
import com.sc2006.g5.edufinder.dto.response.VoteSummaryResponse;
import com.sc2006.g5.edufinder.model.comment.Comment;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ReplyMapper replyMapper;
    private final VoteSummaryMapper voteSummaryMapper;

    public CommentResponse toCommentResponse(Long userId, Comment comment) {
        Long commentId = comment.getId();

        List<ReplyResponse> replies = comment.getReplies().stream()
            .map(replyMapper::toReplyResponse)
            .toList();

        VoteSummaryResponse summary = voteSummaryMapper.buildVoteSummary(userId, commentId);

        return CommentResponse.builder()
            .id(commentId)
            .content(comment.getContent())
            .username(comment.getUser().getUsername())
            .createdAt(comment.getCreatedAt())
            .replies(replies)
            .voteSummary(summary)
            .build();
    }
}

