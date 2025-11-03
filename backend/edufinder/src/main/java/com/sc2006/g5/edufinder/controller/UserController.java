package com.sc2006.g5.edufinder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sc2006.g5.edufinder.dto.request.SavedSchoolRequest;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;
import com.sc2006.g5.edufinder.model.user.CustomUserDetails;
import com.sc2006.g5.edufinder.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users/saved-schools")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public SavedSchoolResponse getSavedSchoolIds(Authentication auth){
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Long userId = user.getId();

        return userService.getSavedSchoolIds(userId);
    }

    @PostMapping
    public void addSavedSchool(Authentication auth, @Valid @RequestBody SavedSchoolRequest savedSchoolRequest){
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Long userId = user.getId();

        userService.addSavedSchool(userId, savedSchoolRequest);
    }

    @DeleteMapping
    public void removeSavedSchool(Authentication auth, @Valid @RequestBody SavedSchoolRequest savedSchoolRequest){
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Long userId = user.getId();

        userService.removeSavedSchool(userId, savedSchoolRequest);
    }
}
