package com.example.campusrideshare.api;

import com.example.campusrideshare.dto.RideSignupDTO;
import com.example.campusrideshare.messaging.RideEventMessage;
import com.example.campusrideshare.messaging.RideEventPublisher;
import com.example.campusrideshare.model.Ride;
import com.example.campusrideshare.model.RideSignup;
import com.example.campusrideshare.model.User;
import com.example.campusrideshare.repository.RideRepository;
import com.example.campusrideshare.repository.RideSignupRepository;
import com.example.campusrideshare.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class RideSignupRestController {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final RideSignupRepository rideSignupRepository;
    private final RideEventPublisher rideEventPublisher;

    public RideSignupRestController(RideRepository rideRepository,
                                    UserRepository userRepository,
                                    RideSignupRepository rideSignupRepository,
                                    RideEventPublisher rideEventPublisher) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.rideSignupRepository = rideSignupRepository;
        this.rideEventPublisher = rideEventPublisher;
    }

    @PostMapping
    public String signup(@RequestBody RideSignupDTO dto) {

        Ride ride = rideRepository.findById(dto.getRideId()).orElseThrow();
        User passenger = userRepository.findById(dto.getPassengerId()).orElseThrow();

        if (ride.getAvailableSeats() <= 0) {
            return "Ride is full.";
        }

        // Save signup
        RideSignup signup = new RideSignup();
        signup.setRide(ride);
        signup.setPassenger(passenger);
        rideSignupRepository.save(signup);

        // Update seats
        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideRepository.save(ride);

        // ----- Publish PASSENGER_SIGNUP event -----
        RideEventMessage signupMsg = new RideEventMessage();
        signupMsg.setType("PASSENGER_SIGNUP");
        signupMsg.setRideId(ride.getId());
        signupMsg.setRideFrom(ride.getFromLocation());
        signupMsg.setRideToCampus(ride.getToCampus());
        signupMsg.setDepartureTime(ride.getDepartureTime());
        signupMsg.setPassengerId(passenger.getId());
        signupMsg.setPassengerName(passenger.getFullName());
        signupMsg.setPassengerEmail(passenger.getEmail());

        rideEventPublisher.sendEvent(signupMsg);

        // ----- If ride just became full, publish RIDE_FULL event -----
        if (ride.getAvailableSeats() == 0) {
            RideEventMessage fullMsg = new RideEventMessage();
            fullMsg.setType("RIDE_FULL");
            fullMsg.setRideId(ride.getId());
            fullMsg.setRideFrom(ride.getFromLocation());
            fullMsg.setRideToCampus(ride.getToCampus());
            fullMsg.setDepartureTime(ride.getDepartureTime());

            rideEventPublisher.sendEvent(fullMsg);
        }

        return "Passenger added to ride.";
    }
}
