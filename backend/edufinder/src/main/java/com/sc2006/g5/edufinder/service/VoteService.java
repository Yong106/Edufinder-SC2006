package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.SetVoteRequest;

public interface VoteService {
    void setVote(Long userId, Long commentId, SetVoteRequest setVoteRequest);
}
