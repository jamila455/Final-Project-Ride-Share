package com.example.campusrideshare.messaging;

import java.time.LocalDateTime;

public class RideEventMessage {

    // Example types: RIDE_FULL, RIDE_CANCELLED, PASSENGER_SIGNUP
    private String type;

    private Long rideId;
    private String rideFrom;
    private String rideToCampus;
    private LocalDateTime departureTime;

    private Long passengerId;
    private String passengerName;
    private String passengerEmail;

    public RideEventMessage() {
    }

    // getters and setters...

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public String getRideFrom() {
        return rideFrom;
    }

    public void setRideFrom(String rideFrom) {
        this.rideFrom = rideFrom;
    }

    public String getRideToCampus() {
        return rideToCampus;
    }

    public void setRideToCampus(String rideToCampus) {
        this.rideToCampus = rideToCampus;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }
}
