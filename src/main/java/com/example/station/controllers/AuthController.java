package com.example.station.controllers;

import com.example.station.pojo.RegisterRequest;
import com.example.station.service.SupabaseAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private SupabaseAuthService supabaseAuthService;

    @PostMapping("/register")
    public Mono<String> register(@RequestBody RegisterRequest request) {
        return supabaseAuthService.registerUser(request.getEmail(), request.getPassword());
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestBody RegisterRequest request) {
        return supabaseAuthService.loginUser(request.getEmail(), request.getPassword());
    }
}
