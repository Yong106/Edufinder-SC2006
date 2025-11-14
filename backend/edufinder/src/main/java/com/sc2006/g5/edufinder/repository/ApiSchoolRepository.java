package com.sc2006.g5.edufinder.repository;

import java.util.List;

import com.sc2006.g5.edufinder.model.school.ApiSchool;

/**
 * Custom repository to access and manage {@link ApiSchool}.
 *
 * @implNote Implementations should load the initial data at application startup
 *           and schedule periodic asynchronous refreshes at an appropriate interval.
 */
public interface ApiSchoolRepository {

    /**
     * Get all school information from API.
     * @return all {@link ApiSchool} from the API.
     */
    List<ApiSchool> getApiSchools();

    /**
     * Refresh all school information from API.
     */
    void refreshApiSchools();
}
