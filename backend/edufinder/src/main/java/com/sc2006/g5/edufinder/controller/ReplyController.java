package com.sc2006.g5.edufinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;
import com.sc2006.g5.edufinder.model.CustomUserDetails;
import com.sc2006.g5.edufinder.service.ReplyService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("api/replies")
public class ReplyController {
    
    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService){
        this.replyService = replyService;
    }

    @PostMapping
    public void createReply(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody CreateReplyRequest request){
        Long userId = user.getId();
        replyService.createReply(userId, request);
    }

    @DeleteMapping("/{replyId}")
    public void deleteReply(@AuthenticationPrincipal CustomUserDetails user, @PathVariable @NotNull Long replyId){
        Long userId = user.getId();

        replyService.deleteReply(userId, replyId);
    }
}
