package com.sc2006.g5.edufinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;
import com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException;
import com.sc2006.g5.edufinder.exception.security.AccessDeniedException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.model.Comment;
import com.sc2006.g5.edufinder.model.Reply;
import com.sc2006.g5.edufinder.model.User;
import com.sc2006.g5.edufinder.repository.CommentRepository;
import com.sc2006.g5.edufinder.repository.ReplyRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;

@Service
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository, UserRepository userRepository, CommentRepository commentRepository){
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void createReply(Long userId, CreateReplyRequest createReplyRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        Long commentId = createReplyRequest.getCommentId();
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));

        Reply reply = Reply.builder()
            .content(createReplyRequest.getContent())
            .user(user)
            .comment(comment)
            .build();

        replyRepository.save(reply);
    }

    @Override
    public void deleteReply(Long userId, Long replyId) {
        Reply reply = replyRepository.findOneByIdAndUserId(replyId, userId)
            .orElseThrow(() -> new AccessDeniedException());

        replyRepository.delete(reply);
    }
    
}
