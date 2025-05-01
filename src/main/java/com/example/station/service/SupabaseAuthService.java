package com.example.station.service;

import com.example.station.models.User;
import com.example.station.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Service
public class SupabaseAuthService {

    private final WebClient webClient;
    @Autowired
    private UserRepository userRepository;
    private final String supabaseKey; // сохраняем отдельно ключ

    public SupabaseAuthService(
            @Value("${supabase.api.url}") String supabaseUrl,
            @Value("${supabase.api.key}") String supabaseKey) {
        this.supabaseKey = supabaseKey;
        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl + "/auth/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
                .defaultHeader("apikey", supabaseKey)
                .build();
    }

//    public Mono<String> registerUser(String email, String password) {
//        return webClient.post()
//                .uri("/signup")
//                .bodyValue("""
//                        {
//                            "email": "%s",
//                            "password": "%s"
//                        }
//                        """.formatted(email, password))
//                .retrieve()
//                .bodyToMono(String.class);
//    }
public Mono<String> registerUser(String email, String password) {
    return Mono.fromCallable(() -> {
                Optional<User> existingUser = userRepository.findByEmail(email);
                if (existingUser.isPresent()) {
                    throw new RuntimeException("User with this email already exists");
                }

                return null;
            })
            .then(
                    webClient.post()
                            .uri("/signup")
                            .bodyValue("""
                                {
                                    "email": "%s",
                                    "password": "%s"
                                }
                                """.formatted(email, password))
                            .retrieve()
                            .bodyToMono(String.class)
                            .flatMap(response -> {
                                User newUser = new User();
                                newUser.setEmail(email);
                                newUser.setPassword(password);

                                userRepository.save(newUser);
                                return Mono.just(response);
                            })
            );
}
    public Mono<String> loginUser(String email, String password) {
        return webClient.post()
                .uri("/token?grant_type=password")
                .bodyValue("""
                        {
                            "email": "%s",
                            "password": "%s"
                        }
                        """.formatted(email, password))
                .retrieve()
                .bodyToMono(String.class);
    }
}