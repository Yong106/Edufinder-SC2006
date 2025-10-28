package com.sc2006.g5.edufinder.service;

import com.sc2006.g5.edufinder.dto.request.LoginRequest;
import com.sc2006.g5.edufinder.dto.request.SignupRequest;

public interface AuthService {
    
    public String login(LoginRequest loginRequest);
    public String signup(SignupRequest signupRequest);

} 