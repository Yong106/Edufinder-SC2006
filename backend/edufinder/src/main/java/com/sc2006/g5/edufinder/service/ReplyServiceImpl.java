package com.sc2006.g5.edufinder.service;

import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;
import com.sc2006.g5.edufinder.dto.response.ReplyResponse;
import com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException;
import com.sc2006.g5.edufinder.exception.security.AccessDeniedException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.mapper.ReplyMapper;
import com.sc2006.g5.edufinder.model.User;
import com.sc2006.g5.edufinder.model.comment.Comment;
import com.sc2006.g5.edufinder.model.comment.Reply;
import com.sc2006.g5.edufinder.repository.CommentRepository;
import com.sc2006.g5.edufinder.repository.ReplyRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReplyMapper replyMapper;

    @Override
    public ReplyResponse createReply(Long userId, Long commentId, CreateReplyRequest createReplyRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));

        Reply reply = Reply.builder()
            .content(createReplyRequest.getContent())
            .user(user)
            .comment(comment)
            .build();

        replyRepository.save(reply);

        return replyMapper.toReplyResponse(reply);
    }

    @Override
    public void deleteReply(Long userId, Long replyId) {
        Reply reply = replyRepository.findOneByIdAndUserId(replyId, userId)
            .orElseThrow(() -> new AccessDeniedException());

        replyRepository.delete(reply);
    }
    
}
