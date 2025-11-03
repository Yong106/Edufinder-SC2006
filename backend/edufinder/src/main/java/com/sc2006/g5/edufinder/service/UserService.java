package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.SavedSchoolRequest;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;
import com.sc2006.g5.edufinder.dto.response.UserResponse;

public interface UserService {
    UserResponse getUserByUsername(String username);
    SavedSchoolResponse getSavedSchoolIds(Long userId);
    void addSavedSchool(Long userId, SavedSchoolRequest savedSchoolRequest);
    void removeSavedSchool(Long userId, SavedSchoolRequest savedSchoolRequest);
} 