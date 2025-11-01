package com.sc2006.g5.edufinder.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.dto.request.CreateCommentRequest;
import com.sc2006.g5.edufinder.dto.response.CommentResponse;
import com.sc2006.g5.edufinder.dto.response.CommentsResponse;
import com.sc2006.g5.edufinder.model.CustomUserDetails;
import com.sc2006.g5.edufinder.service.CommentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;

    @GetMapping("/schools/{schoolId}/comments")
    public CommentsResponse getCommentsBySchoolId(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable @NotNull Long schoolId
    ) {

        Long userId = (user != null) ? user.getId() : null;
        return commentService.getCommentsBySchoolId(userId, schoolId);
    }

    @PostMapping("/schools/{schoolId}/comments")
    public CommentResponse createComment(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable @NotNull Long schoolId,
            @Valid @RequestBody CreateCommentRequest request    
        ) {

        Long userId = user.getId();
        return commentService.createComment(userId, schoolId, request);
    }

    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable @NotNull Long commentId
    ) {

        Long userId = user.getId();
        commentService.deleteComment(userId, commentId);
    }
}
