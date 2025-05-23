package com.zephyra.station.service;

import com.zephyra.station.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SupabaseAuthService authService;


    public Mono<String> changeUsername(String newUsername) {
        if (newUsername == null || newUsername.isBlank()) {
            return Mono.error(new IllegalArgumentException("New username must be provided"));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String supabaseId = jwt.getClaimAsString("sub");
        return Mono.justOrEmpty(userRepository.findBySupabaseId(supabaseId))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> {
                    user.setUsername(newUsername);
                    userRepository.save(user);
                    return Mono.just("Username changed successfully");
                });
    }
}