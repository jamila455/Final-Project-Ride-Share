package com.example.campusrideshare.controller;

import com.example.campusrideshare.model.Ride;
import com.example.campusrideshare.model.User;
import com.example.campusrideshare.repository.RideRepository;
import com.example.campusrideshare.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/rides")
public class RideController {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public RideController(RideRepository rideRepository, UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    // GET /rides  -> list upcoming rides
    @GetMapping
    public String listRides(@RequestParam(required = false) String campus, Model model) {
        List<Ride> rides;

        if (campus != null && !campus.isBlank()) {
            rides = rideRepository.findByToCampusAndDepartureTimeAfter(campus, LocalDateTime.now());
        } else {
            rides = rideRepository.findByDepartureTimeAfter(LocalDateTime.now());
        }

        model.addAttribute("rides", rides);
        model.addAttribute("selectedCampus", campus);

        return "rides/list";
    }

    // GET /rides/create  -> show form
    @GetMapping("/create")
    public String showCreateForm() {
        // plain HTML form, no model object needed
        return "rides/create";
    }

    // POST /rides/create -> process form from plain HTML fields
    @PostMapping("/create")
    public String handleCreate(
            @RequestParam String fromLocation,
            @RequestParam String toCampus,
            @RequestParam String departureTime,
            @RequestParam int totalSeats,
            @RequestParam(required = false) Integer availableSeats,
            @RequestParam(required = false) String notes,
            @RequestParam("driverName") String driverName,
            @RequestParam("driverEmail") String driverEmail
    ) {

        // get or create driver based on email
        User driver = userRepository.findByEmail(driverEmail)
                .orElseGet(() -> {
                    User u = new User();
                    u.setFullName(driverName);
                    u.setEmail(driverEmail);
                    u.setPassword("temp"); // TODO: replace with real registration + hashed password
                    u.setRole("ROLE_DRIVER");
                    return userRepository.save(u);
                });

        // build the Ride object from form fields
        Ride ride = new Ride();
        ride.setFromLocation(fromLocation);
        ride.setToCampus(toCampus);

        // browser sends datetime-local like "2025-11-16T19:30"
        ride.setDepartureTime(LocalDateTime.parse(departureTime));

        ride.setTotalSeats(totalSeats);
        if (availableSeats == null || availableSeats <= 0) {
            ride.setAvailableSeats(totalSeats);
        } else {
            ride.setAvailableSeats(availableSeats);
        }

        ride.setNotes(notes);
        ride.setDriver(driver);

        rideRepository.save(ride);

        return "redirect:/rides";
    }
}
