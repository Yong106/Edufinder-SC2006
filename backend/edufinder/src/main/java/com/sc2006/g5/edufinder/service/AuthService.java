package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.LoginRequest;
import com.sc2006.g5.edufinder.dto.request.SignupRequest;

public interface AuthService {
    
    String login(LoginRequest loginRequest);
    String signup(SignupRequest signupRequest);

} 