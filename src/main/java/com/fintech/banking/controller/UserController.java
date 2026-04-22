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

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request.getEmail(), request.getPassword()));
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        return userService.verifyEmail(token);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        return userService.forgotPassword(email);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String newPassword) {
        return userService.resetPassword(token, newPassword);
    }

    @GetMapping("/{userId}")
    public UserResponse getProfile(@PathVariable String userId) {
        return userService.getUserProfile(userId);
    }

    @GetMapping("/{userId}/validate")
    public Boolean validateUser(@PathVariable String userId) {
        return userService.existByUserId(userId);
    }

    @PostMapping("/logout")
    public String logout(@RequestParam String userId) {
        return userService.logout(userId);
    }
}