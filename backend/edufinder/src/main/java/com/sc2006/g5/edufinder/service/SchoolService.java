package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.EditSchoolCutOffPointRequest;
import com.sc2006.g5.edufinder.dto.response.SchoolsResponse;

/**
 * A service responsible for handling school-related operations.
 * <p>
 * Provides methods for getting all schools and edit schools cut-off point
 */
public interface SchoolService {

    /**
     * Get all schools, including data such as general information, subjects, CCAs, and programmes.
     *
     * @return a response object containing all schools information
     *
     * @see SchoolsResponse
     */
    SchoolsResponse getAllSchools();

    /**
     * Edit a school's minimum and maximum cut-off point.
     *
     * @param schoolId id of the school
     * @param editSchoolCutOffPointRequest request object containing data required to edit cut-off point
     *
     * @throws com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException if school with `schoolId` is not found
     * @throws com.sc2006.g5.edufinder.exception.school.CutOffPointException if minimum cut-off point larger than maximum cut-off point
     *
     * @see EditSchoolCutOffPointRequest
     */
    void editSchoolCutOffPoint(Long schoolId, EditSchoolCutOffPointRequest editSchoolCutOffPointRequest);
} 