package com.sc2006.g5.edufinder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sc2006.g5.edufinder.model.comment.Reply;

/**
 * JPA Repository for {@link Reply}
 */
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findOneByIdAndUserId(Long id, Long userId);
}
