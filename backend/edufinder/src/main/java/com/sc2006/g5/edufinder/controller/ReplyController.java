package com.sc2006.g5.edufinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.dto.request.CreateReplyRequest;
import com.sc2006.g5.edufinder.dto.response.ReplyResponse;
import com.sc2006.g5.edufinder.model.user.CustomUserDetails;
import com.sc2006.g5.edufinder.service.ReplyService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ReplyController {
    
    private final ReplyService replyService;

    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<?> createReply(
        @AuthenticationPrincipal CustomUserDetails user, 
        @NotNull @PathVariable Long commentId,
        @Valid @RequestBody CreateReplyRequest request
    ){
        Long userId = user.getId();
        ReplyResponse response = replyService.createReply(userId, commentId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<?> deleteReply(
        @AuthenticationPrincipal CustomUserDetails user,
        @PathVariable @NotNull Long replyId
    ){
        Long userId = user.getId();
        replyService.deleteReply(userId, replyId);

        return ResponseEntity.noContent().build();
    }
}
