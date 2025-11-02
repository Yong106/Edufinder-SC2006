package com.sc2006.g5.edufinder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.request.CreateCommentRequest;
import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.CommentsResponse;
import com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException;
import com.sc2006.g5.edufinder.exception.security.AccessDeniedException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.mapper.CommentMapper;
import com.sc2006.g5.edufinder.model.school.DbSchool;
import com.sc2006.g5.edufinder.model.User;
import com.sc2006.g5.edufinder.model.comment.Comment;
import com.sc2006.g5.edufinder.repository.CommentRepository;
import com.sc2006.g5.edufinder.repository.DbSchoolRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DbSchoolRepository schoolRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentsResponse getCommentsBySchoolId(Long userId, Long schoolId) {
        if(!schoolRepository.existsById(schoolId)){
            throw new SchoolNotFoundException(schoolId);
        }

        List<Comment> comments = commentRepository.findAllBySchoolId(schoolId);
        List<CommentResponse> commentResponses = comments.stream()
            .map(comment -> commentMapper.toCommentResponse(userId, comment))
            .toList();

        return CommentsResponse.builder()
            .comments(commentResponses)
            .build();
    }

    @Override
    public CommentResponse createComment(Long userId, Long schoolId, CreateCommentRequest createCommentRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        DbSchool school = schoolRepository.findById(schoolId)
            .orElseThrow(() -> new SchoolNotFoundException(schoolId));

        Comment comment = Comment.builder()
            .content(createCommentRequest.getContent())
            .user(user)
            .school(school)
            .build();

        commentRepository.save(comment);

        return commentMapper.toCommentResponse(userId, comment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findOneByIdAndUserId(commentId, userId)
            .orElseThrow(AccessDeniedException::new);

        commentRepository.delete(comment);
    }
    
}
