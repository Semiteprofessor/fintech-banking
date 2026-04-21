package com.fintech.banking.service;

import com.fintech.banking.dto.RegisterRequest;
import com.fintech.banking.dto.UserResponse;
import com.fintech.banking.model.User;
import com.fintech.banking.repository.UserRepository;
import com.fintech.banking.security.JwtUtil;
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

    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerified(false);

        repository.save(user);

        // TODO: send verification email

        // 🔐 return JWT immediately OR after verification (your choice)
        return jwtUtil.generateToken(user.getEmail());
    }

    public String login(String email, String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isVerified()) {
            throw new RuntimeException("Email not verified");
        }

        // 🔐 GENERATE JWT TOKEN
        return jwtUtil.generateToken(user.getEmail());
    }

    // =========================
    // LOGOUT (JWT SYSTEM)
    // =========================
    public String logout(String userId) {
        log.info("User logged out: {}", userId);

        // NOTE: JWT cannot be invalidated unless blacklist system exists
        return "Logout successful (client must delete token)";
    }

    // =========================
    // EMAIL VERIFICATION
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

        // TODO: send email

        return "Password reset link sent";
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
    // VALIDATION
    // =========================
    public Boolean existByUserId(String userId) {
        log.info("Validating user: {}", userId);
        return repository.existsById(userId);
    }

    // =========================
    // MAPPER
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