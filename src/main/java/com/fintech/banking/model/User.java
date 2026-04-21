package com.fintech.banking.model;

import jakarta.persistence.*;
import lombok.Data;

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


    @OneToMany(mappedBy = "user")
    private List<Account> accounts;
}