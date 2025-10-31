package com.sc2006.g5.edufinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.request.SetVoteRequest;
import com.sc2006.g5.edufinder.exception.comment.CommentNotFoundException;
import com.sc2006.g5.edufinder.exception.user.UserNotFoundException;
import com.sc2006.g5.edufinder.model.User;
import com.sc2006.g5.edufinder.model.comment.Comment;
import com.sc2006.g5.edufinder.model.comment.Vote;
import com.sc2006.g5.edufinder.model.comment.VoteId;
import com.sc2006.g5.edufinder.repository.CommentRepository;
import com.sc2006.g5.edufinder.repository.UserRepository;
import com.sc2006.g5.edufinder.repository.VoteRepository;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository, UserRepository userRepository, CommentRepository commentRepository){
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public void setVote(Long userId, SetVoteRequest setVoteRequest) {
        Long commentId = setVoteRequest.getCommentId();
        VoteId voteId = new VoteId(commentId, userId);

        Vote vote = voteRepository.findById(voteId)
            .orElse(null);

        if(vote == null){
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

            Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

            vote = Vote.builder()
                .id(voteId)
                .user(user)
                .comment(comment)
                .build();
        }

        vote.setVoteType(setVoteRequest.getVoteType());

        voteRepository.save(vote);
    }
    
}
