package com.sc2006.g5.edufinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.dto.request.SetVoteRequest;
import com.sc2006.g5.edufinder.model.user.CustomUserDetails;
import com.sc2006.g5.edufinder.service.VoteService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/comments/{commentId}/votes")
@RequiredArgsConstructor
public class VoteController {
    
    private final VoteService voteService;
    
    @PutMapping
    public ResponseEntity<?> setVote(
        @AuthenticationPrincipal CustomUserDetails user,
        @NotNull @PathVariable Long commentId,
        @Valid @RequestBody SetVoteRequest setVoteRequest
    ){

        Long userId = user.getId();
        voteService.setVote(userId, commentId, setVoteRequest);

        return ResponseEntity.noContent().build();
    }
}
