package com.example.campusrideshare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Where the ride starts (e.g., "Downtown", "South Campus")
    @Column(nullable = false)
    private String fromLocation;

    // Which campus it is going to (e.g., "Elkhorn Campus")
    @Column(nullable = false)
    private String toCampus;

    // Date and time of departure
    @Column(nullable = false)
    private LocalDateTime departureTime;

    // Total number of seats the driver is offering
    @Column(nullable = false)
    private int totalSeats;

    // How many seats are still free
    @Column(nullable = false)
    private int availableSeats;

    // Optional notes like "Meet at main parking lot"
    private String notes;

    // Driver of this ride
    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    public Ride() {
    }

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToCampus() {
        return toCampus;
    }

    public void setToCampus(String toCampus) {
        this.toCampus = toCampus;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }
}
