package com.sc2006.g5.edufinder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sc2006.g5.edufinder.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllBySchoolId(Long schoolId);
    Optional<Comment> findOneByIdAndUserId(Long id, Long userId);
}