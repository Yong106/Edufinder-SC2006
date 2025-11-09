package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.LoginRequest;
import com.sc2006.g5.edufinder.dto.request.SignupRequest;

/**
 * A service responsible for handling user authentication operations.
 * <p>
 * Provides methods for logging in existing users and registering new users.
 */
public interface AuthService {

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param loginRequest the request object containing user credentials
     * @return an authentication token as a {@code String} if authentication is successful
     *
     * @throws com.sc2006.g5.edufinder.exception.auth.InvalidCredentialsException if the credentials are invalid
     *
     * @see LoginRequest
     */
    String login(LoginRequest loginRequest);

    /**
     * Registers a new user with the provided signup details.
     *
     * @param signupRequest the request object containing user information required for registration
     * @return an authentication token as a {@code String} if registration is successful
     *
     * @throws com.sc2006.g5.edufinder.exception.auth.DuplicateUsernameException if a user with the same username already exists
     *
     * @see SignupRequest
     */
    String signup(SignupRequest signupRequest);
}