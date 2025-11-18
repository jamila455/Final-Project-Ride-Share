package com.example.campusrideshare.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // table name in DB
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Example: "Jamila Rahimi"
    @Column(nullable = false)
    private String fullName;

    // Used as login username
    @Column(nullable = false, unique = true)
    private String email;

    // Encrypted password will go here later when we add Security
    @Column(nullable = false)
    private String password;

    // For Spring Security later (ROLE_USER, ROLE_ADMIN, etc.)
    @Column(nullable = false)
    private String role = "ROLE_USER";

    // A user can be a driver for many rides
    @OneToMany(mappedBy = "driver")
    private List<Ride> ridesAsDriver = new ArrayList<>();

    public User() {
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Ride> getRidesAsDriver() {
        return ridesAsDriver;
    }

    public void setRidesAsDriver(List<Ride> ridesAsDriver) {
        this.ridesAsDriver = ridesAsDriver;
    }
}
