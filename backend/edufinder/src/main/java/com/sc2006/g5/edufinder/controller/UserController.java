package com.sc2006.g5.edufinder.controller;

import com.sc2006.g5.edufinder.dto.request.EditUserRequest;
import com.sc2006.g5.edufinder.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
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
    public UserResponse editUser(
        @AuthenticationPrincipal CustomUserDetails user,
        @Valid @RequestBody EditUserRequest editUserRequest
    ){
        Long userId = user.getId();

        return userService.editUser(userId, editUserRequest);
    }

    @GetMapping("/saved-schools")
    public SavedSchoolResponse getSavedSchoolIds(@AuthenticationPrincipal CustomUserDetails user){
        Long userId = user.getId();

        return userService.getSavedSchoolIds(userId);
    }

    @PostMapping("/saved-schools")
    public void addSavedSchool(
        @AuthenticationPrincipal CustomUserDetails user,
        @Valid @RequestBody SavedSchoolRequest savedSchoolRequest
    ){
        Long userId = user.getId();

        userService.addSavedSchool(userId, savedSchoolRequest);
    }

    @DeleteMapping("/saved-schools")
    public void removeSavedSchool(
        @AuthenticationPrincipal CustomUserDetails user,
        @Valid @RequestBody SavedSchoolRequest savedSchoolRequest
    ){
        Long userId = user.getId();

        userService.removeSavedSchool(userId, savedSchoolRequest);
    }
}
