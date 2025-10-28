package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.SavedSchoolRequest;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;

public interface UserService {
    public SavedSchoolResponse getSavedSchoolIds(Long userId);
    public void addSavedSchool(Long userId, SavedSchoolRequest savedSchoolRequest);
    public void removeSavedSchool(Long userId, SavedSchoolRequest savedSchoolRequest);
} 