package com.fintech.banking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String password;

    // =========================
    // EMAIL VERIFICATION
    // =========================
    private String verificationToken;
    private boolean verified = false;

    // =========================
    // PASSWORD RESET
    // =========================
    private String resetPasswordToken;

    // =========================
    // RELATIONSHIP
    // =========================
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;

    // =========================
    // AUDIT FIELDS
    // =========================
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}