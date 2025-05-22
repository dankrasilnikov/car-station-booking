package com.zephyra.station.controllers;

import com.zephyra.station.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getUsernameBySupabaseId(@RequestParam("supabaseId") String supabaseId) {
        return userRepository.findBySupabaseId(supabaseId)
                .map(user -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("username", user.getUsername());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
