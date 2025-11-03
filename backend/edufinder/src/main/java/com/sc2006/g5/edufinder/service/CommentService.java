package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.CreateCommentRequest;
import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.CommentsResponse;

public interface CommentService {
    CommentsResponse getCommentsBySchoolId(Long userId, Long schoolId);
    CommentResponse createComment(Long userId, Long schoolId, CreateCommentRequest createCommentRequest);
    void deleteComment(Long userId, Long commentId);
}