package com.skala.skala_shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skala.skala_shopping.dto.customer.LoginRequest;
import com.skala.skala_shopping.dto.customer.LoginResponse;
import com.skala.skala_shopping.dto.customer.SignUpRequest;
import com.skala.skala_shopping.dto.customer.SignUpResponse;
import com.skala.skala_shopping.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}