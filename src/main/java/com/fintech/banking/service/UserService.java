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

    private EmailService emailService;

    public String register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerified(false);

        repository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), token);

        return "User registered successfully. Please check your email to verify account.";
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

        return jwtUtil.generateToken(user.getEmail());
    }

    public String logout(String userId) {
        log.info("User logged out: {}", userId);

        return "Logout successful (client must delete token)";
    }

    public String verifyEmail(String token) {

        User user = repository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setVerified(true);
        user.setVerificationToken(null);

        repository.save(user);

        return "Email verified successfully";
    }

    public String forgotPassword(String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);

        repository.save(user);

        // TODO: send email

        return "Password reset link sent";
    }

    public String resetPassword(String token, String newPassword) {

        User user = repository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);

        repository.save(user);

        return "Password reset successful";
    }

    public UserResponse getUserProfile(String userId) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return getUserResponse(user);
    }

    public Boolean existByUserId(String userId) {
        log.info("Validating user: {}", userId);
        return repository.existsById(userId);
    }

    private UserResponse getUserResponse(User user) {

        UserResponse response = new UserResponse();
        response.setId(user.getUserId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}