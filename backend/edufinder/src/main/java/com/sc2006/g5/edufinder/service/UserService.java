package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.EditUserRequest;
import com.sc2006.g5.edufinder.dto.request.EditUserRoleRequest;
import com.sc2006.g5.edufinder.dto.request.SavedSchoolRequest;
import com.sc2006.g5.edufinder.dto.response.SavedSchoolResponse;
import com.sc2006.g5.edufinder.dto.response.UserResponse;

/**
 * A service responsible for handling user-related operations.
 * <p>
 * Provides methods for getting user, editing user, and managing saved-school
 */
public interface UserService {

    /**
     * Get a user by username.
     *
     * @param username username of the user
     * @return a response object containing user information
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with {@code username} is not found
     *
     * @see UserResponse
     */
    UserResponse getUserByUsername(String username);

    /**
     * Edit a user's profile.
     *
     * @param userId id of the user
     * @param editUserRequest request object containing data required to edit profile
     *
     * @return a response object containing edited user information
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with {@code userId} is not found
     *
     * @see EditUserRequest
     * @see UserResponse
     */
    UserResponse editUser(Long userId, EditUserRequest editUserRequest);

    /**
     * Edit a user's role.
     *
     * @param userId id of the user
     * @param editUserRoleRequest request object containing data required to edit role
     *
     * @return a response object containing edited user information
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with {@code userId} is not found
     * @throws com.sc2006.g5.edufinder.exception.user.LastAdminException if user is the last admin and is demoting to user
     *
     * @see EditUserRoleRequest
     * @see UserResponse
     */
    UserResponse editUserRole(Long userId, EditUserRoleRequest editUserRoleRequest);

    /**
     * Get all schools saved by a user.
     *
     * @param userId id of the user
     * @return a response object containing id of all saved school.
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with {@code userId} is not found
     *
     * @see SavedSchoolResponse
     */
    SavedSchoolResponse getSavedSchoolIds(Long userId);

    /**
     * Add a school to user's saved schools.
     *
     * @param userId id of the user
     * @param savedSchoolRequest request object containing data required to add saved school
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with {@code userId} is not found
     * @throws com.sc2006.g5.edufinder.exception.school.SchoolNotFoundException if school to save is not found
     * @throws com.sc2006.g5.edufinder.exception.user.UserAlreadySaveSchoolException if user already save school
     *
     * @see SavedSchoolRequest
     */
    void addSavedSchool(Long userId, SavedSchoolRequest savedSchoolRequest);

    /**
     * Remove a school from user's saved schools.
     *
     * @param userId id of the user
     * @param savedSchoolRequest request object containing data required to remove saved school
     *
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotFoundException if user with {@code userId} is not found
     * @throws com.sc2006.g5.edufinder.exception.user.UserNotSaveSchoolException if school is not saved by user
     *
     * @see SavedSchoolRequest
     */
    void removeSavedSchool(Long userId, SavedSchoolRequest savedSchoolRequest);
} 