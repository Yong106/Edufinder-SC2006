package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.dto.request.EditUserRequest;
import com.sc2006.g5.edufinder.dto.request.EditUserRoleRequest;
import com.sc2006.g5.edufinder.dto.response.UserResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sc2006.g5.edufinder.dto.request.SavedSchoolRequest;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;
import com.sc2006.g5.edufinder.model.user.CustomUserDetails;
import com.sc2006.g5.edufinder.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping
    public ResponseEntity<UserResponse> editUser(
        @AuthenticationPrincipal CustomUserDetails user,
        @Valid @RequestBody EditUserRequest editUserRequest
    ){
        Long userId = user.getId();
        UserResponse userResponse = userService.editUser(userId, editUserRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> editUserRole(
            @NotNull @PathVariable Long userId,
            @Valid @RequestBody EditUserRoleRequest editUserRoleRequest
    ){
        UserResponse userResponse = userService.editUserRole(userId, editUserRoleRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/saved-schools")
    public ResponseEntity<SavedSchoolResponse> getSavedSchoolIds(
        @AuthenticationPrincipal CustomUserDetails user
    ){
        Long userId = user.getId();

        SavedSchoolResponse response = userService.getSavedSchoolIds(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/saved-schools")
    public ResponseEntity<?> addSavedSchool(
        @AuthenticationPrincipal CustomUserDetails user,
        @Valid @RequestBody SavedSchoolRequest savedSchoolRequest
    ){
        Long userId = user.getId();
        userService.addSavedSchool(userId, savedSchoolRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/saved-schools")
    public ResponseEntity<?> removeSavedSchool(
        @AuthenticationPrincipal CustomUserDetails user,
        @Valid @RequestBody SavedSchoolRequest savedSchoolRequest
    ){
        Long userId = user.getId();
        userService.removeSavedSchool(userId, savedSchoolRequest);
        return ResponseEntity.noContent().build();
    }
}
