package com.sc2006.g5.edufinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sc2006.g5.edufinder.model.comment.Vote;
import com.sc2006.g5.edufinder.model.comment.VoteId;
import com.sc2006.g5.edufinder.model.comment.VoteType;

public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    long countByIdCommentIdAndVoteType(Long commentId, VoteType voteType);
}
