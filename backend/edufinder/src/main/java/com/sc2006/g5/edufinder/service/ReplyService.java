package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;

public interface ReplyService {
    void createReply(Long userId, CreateReplyRequest createReplyRequest);
    void deleteReply(Long userId, Long replyId);
}
