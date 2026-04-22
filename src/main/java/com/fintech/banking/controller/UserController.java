package com.fintech.banking.controller;

import com.fintech.banking.dto.LoginRequest;
import com.fintech.banking.dto.RegisterRequest;
import com.fintech.banking.dto.UserResponse;
import com.fintech.banking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // =========================
    // LOGIN (RETURN TOKEN)
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        String token = userService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(
                java.util.Map.of("token", token)
        );
    }

    // =========================
    // VERIFY EMAIL
    // =========================
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(userService.verifyEmail(token));
    }

    // =========================
    // FORGOT PASSWORD
    // =========================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    // =========================
    // RESET PASSWORD
    // =========================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        return ResponseEntity.ok(userService.resetPassword(token, newPassword));
    }

    // =========================
    // PROFILE
    // =========================
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.existByUserId(userId));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String userId) {
        return ResponseEntity.ok(userService.logout(userId));
    }
}