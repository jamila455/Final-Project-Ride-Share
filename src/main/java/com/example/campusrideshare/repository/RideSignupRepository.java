package com.example.campusrideshare.repository;

import com.example.campusrideshare.model.Ride;
import com.example.campusrideshare.model.RideSignup;
import com.example.campusrideshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideSignupRepository extends JpaRepository<RideSignup, Long> {

    List<RideSignup> findByPassenger(User passenger);

    List<RideSignup> findByRide(Ride ride);
}
