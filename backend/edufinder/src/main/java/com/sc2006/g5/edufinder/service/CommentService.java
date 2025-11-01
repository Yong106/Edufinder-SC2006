package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.CreateCommentRequest;
import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.CommentsResponse;

public interface CommentService {
    public CommentsResponse getCommentsBySchoolId(Long userId, Long schoolId);
    public CommentResponse createComment(Long userId, Long schoolId, CreateCommentRequest createCommentRequest);
    public void deleteComment(Long userId, Long commentId);
}