package com.sc2006.g5.edufinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sc2006.g5.edufinder.dto.request.LoginRequest;
import com.sc2006.g5.edufinder.dto.request.SignupRequest;
import com.sc2006.g5.edufinder.exception.auth.DuplicateUsernameException;
import com.sc2006.g5.edufinder.exception.auth.InvalidCredentialsException;
import com.sc2006.g5.edufinder.model.user.User;
import com.sc2006.g5.edufinder.repository.UserRepository;
import com.sc2006.g5.edufinder.security.SessionProvider;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionProvider sessionProvider;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, SessionProvider sessionProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.findOneByUsername(username)
            .orElseThrow(InvalidCredentialsException::new);

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialsException();
        }

        return sessionProvider.generateToken(user.getId());
    }

    @Override
    public String signup(SignupRequest signupRequest) {
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();

        User user = userRepository.findOneByUsername(username).orElse(null);
        if(user != null){
            throw new DuplicateUsernameException();
        }

        password = passwordEncoder.encode(password);

        user = User.builder()
            .username(username)
            .password(password)
            .build();

        userRepository.save(user);

        return sessionProvider.generateToken(user.getId());
    }

}
