package com.example.campusrideshare.repository;

import com.example.campusrideshare.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    // All rides that are in the future
    List<Ride> findByDepartureTimeAfter(LocalDateTime now);

    // All rides to a given campus in the future
    List<Ride> findByToCampusAndDepartureTimeAfter(String toCampus, LocalDateTime now);
}
