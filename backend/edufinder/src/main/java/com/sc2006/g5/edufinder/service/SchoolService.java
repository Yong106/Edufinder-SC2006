package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.EditSchoolCutOffPointRequest;
import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;

public interface SchoolService {
    SchoolsResponse getAllSchools();
    void editSchoolCutOffPoint(Long schoolId, EditSchoolCutOffPointRequest editSchoolCutOffPointRequest);
} 