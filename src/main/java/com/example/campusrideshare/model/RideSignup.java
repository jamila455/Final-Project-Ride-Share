package com.example.campusrideshare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ride_signups")
public class RideSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The ride that is being joined
    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private Ride ride;

    // The passenger who joined this ride
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    // When they signed up
    private LocalDateTime signupTime = LocalDateTime.now();

    public RideSignup() {
    }

    public RideSignup(Ride ride, User passenger) {
        this.ride = ride;
        this.passenger = passenger;
        this.signupTime = LocalDateTime.now();
    }

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public LocalDateTime getSignupTime() {
        return signupTime;
    }

    public void setSignupTime(LocalDateTime signupTime) {
        this.signupTime = signupTime;
    }
}
