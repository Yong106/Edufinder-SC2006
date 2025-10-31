package com.sc2006.g5.edufinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.dto.request.SetVoteRequest;
import com.sc2006.g5.edufinder.model.CustomUserDetails;
import com.sc2006.g5.edufinder.service.VoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/votes")
public class VoteController {
    
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService){
        this.voteService = voteService;
    }
    
    @PutMapping
    public void setVote(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody SetVoteRequest setVoteRequest){
        Long userId = user.getId();
        voteService.setVote(userId, setVoteRequest);
    }
}
