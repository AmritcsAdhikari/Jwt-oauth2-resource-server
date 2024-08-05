package com.acsa.service;

import com.acsa.dto.LoginRequest;
import com.acsa.dto.LoginResponse;
import com.acsa.dto.RegisterRequest;
import com.acsa.entity.User;
import com.acsa.repo.UserRepository;
import com.acsa.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public Long registerUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setUsername(registerRequest.username());
        user.setRole("ROLE_USER");

        return userRepository.save(user).getId();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),
                loginRequest.password()));

        String s = jwtProvider.generateToken(authenticate);

        return new LoginResponse(s);
    }

}
