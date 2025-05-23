package com.zephyra.station.controllers;

import com.zephyra.station.repository.UserRepository;
import com.zephyra.station.service.SupabaseAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    SupabaseAuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getUserProfileBySupabaseId(@RequestParam("supabaseId") String supabaseId) {
        return userRepository.findBySupabaseId(supabaseId)
                .map(user -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("username", user.getUsername());
                    response.put("role", user.getRole().name()); // предполагается, что поле `role` — это enum
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/profile/changepass")
    public Mono<ResponseEntity<String>> changePassword(
           @RequestHeader("Authorization") String bearerToken,
            @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");

        return authService.changeUserPassword(bearerToken, newPassword)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

}
