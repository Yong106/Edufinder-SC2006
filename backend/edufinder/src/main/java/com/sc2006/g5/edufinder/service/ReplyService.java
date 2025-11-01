package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;
import com.sc2006.g5.edufinder.dto.response.ReplyResponse;

public interface ReplyService {
    ReplyResponse createReply(Long userId, Long commentId, CreateReplyRequest createReplyRequest);
    void deleteReply(Long userId, Long replyId);
}
