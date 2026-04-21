package com.fintech.banking.service;

import com.fintech.banking.dto.RegisterRequest;
import com.fintech.banking.dto.UserResponse;
import com.fintech.banking.model.User;
import com.fintech.banking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // 🔐 HASH PASSWORD
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 📧 EMAIL VERIFICATION TOKEN
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerified(false);

        User savedUser = repository.save(user);

        // TODO: send verification email here

        return getUserResponse(savedUser);
    }

    public UserResponse login(String email, String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isVerified()) {
            throw new RuntimeException("Email not verified");
        }

        // TODO: generate JWT token here

        return getUserResponse(user);
    }

    // =========================
    // LOGOUT (JWT-based systems invalidate token on client side)
    // =========================
    public String logout(String userId) {
        log.info("User logged out: {}", userId);
        return "Logout successful (client should delete token)";
    }

    // =========================
    // VERIFY EMAIL
    // =========================
    public String verifyEmail(String token) {

        User user = repository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setVerified(true);
        user.setVerificationToken(null);

        repository.save(user);

        return "Email verified successfully";
    }

    // =========================
    // FORGOT PASSWORD
    // =========================
    public String forgotPassword(String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);

        repository.save(user);

        // TODO: send email with reset token

        return "Password reset link sent to email";
    }

    // =========================
    // RESET PASSWORD
    // =========================
    public String resetPassword(String token, String newPassword) {

        User user = repository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);

        repository.save(user);

        return "Password reset successful";
    }

    // =========================
    // PROFILE
    // =========================
    public UserResponse getUserProfile(String userId) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return getUserResponse(user);
    }

    // =========================
    // CHECK USER EXISTS
    // =========================
    public Boolean existByUserId(String userId) {
        log.info("Validating user: {}", userId);
        return repository.existsById(userId);
    }

    // =========================
    // RESPONSE MAPPING
    // =========================
    private UserResponse getUserResponse(User user) {

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}
