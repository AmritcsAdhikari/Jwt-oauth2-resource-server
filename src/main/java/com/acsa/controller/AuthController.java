package com.acsa.controller;

import com.acsa.dto.LoginRequest;
import com.acsa.dto.LoginResponse;
import com.acsa.dto.RegisterRequest;
import com.acsa.dto.RegisterResponse;
import com.acsa.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        Long l = userService.registerUser(registerRequest);
        return new ResponseEntity<>(new RegisterResponse("User registered with id- " + l.toString()), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }

}
