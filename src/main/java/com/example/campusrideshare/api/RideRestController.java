package com.example.campusrideshare.api;

import com.example.campusrideshare.dto.RideDTO;
import com.example.campusrideshare.messaging.RideEventMessage;
import com.example.campusrideshare.messaging.RideEventPublisher;
import com.example.campusrideshare.model.Ride;
import com.example.campusrideshare.model.User;
import com.example.campusrideshare.repository.RideRepository;
import com.example.campusrideshare.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideRestController {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final RideEventPublisher rideEventPublisher;

    public RideRestController(RideRepository rideRepository,
                              UserRepository userRepository,
                              RideEventPublisher rideEventPublisher) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.rideEventPublisher = rideEventPublisher;
    }

    // CREATE RIDE
    @PostMapping
    public RideDTO create(@RequestParam Long driverId, @RequestBody Ride ride) {

        User driver = userRepository.findById(driverId).orElseThrow();
        ride.setDriver(driver);

        Ride saved = rideRepository.save(ride);
        return toDTO(saved);
    }

    // UPDATE RIDE
    @PutMapping("/{id}")
    public RideDTO update(@PathVariable Long id, @RequestBody Ride updated) {
        Ride ride = rideRepository.findById(id).orElseThrow();

        ride.setFromLocation(updated.getFromLocation());
        ride.setToCampus(updated.getToCampus());
        ride.setDepartureTime(updated.getDepartureTime());
        ride.setTotalSeats(updated.getTotalSeats());
        ride.setAvailableSeats(updated.getAvailableSeats());
        ride.setNotes(updated.getNotes());

        Ride saved = rideRepository.save(ride);

        return toDTO(saved);
    }

    // DELETE RIDE (publish RIDE_CANCELLED event)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        // load the ride so we can include info in the message
        Ride ride = rideRepository.findById(id).orElse(null);

        if (ride != null) {
            RideEventMessage msg = new RideEventMessage();
            msg.setType("RIDE_CANCELLED");
            msg.setRideId(ride.getId());
            msg.setRideFrom(ride.getFromLocation());
            msg.setRideToCampus(ride.getToCampus());
            msg.setDepartureTime(ride.getDepartureTime());

            rideEventPublisher.sendEvent(msg);
        }

        rideRepository.deleteById(id);
    }

    // LIST ALL RIDES
    @GetMapping
    public List<RideDTO> getAll() {
        return rideRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    private RideDTO toDTO(Ride ride) {
        RideDTO dto = new RideDTO();
        dto.setId(ride.getId());
        dto.setFromLocation(ride.getFromLocation());
        dto.setToCampus(ride.getToCampus());
        dto.setDepartureTime(ride.getDepartureTime());
        dto.setTotalSeats(ride.getTotalSeats());
        dto.setAvailableSeats(ride.getAvailableSeats());
        dto.setDriverName(ride.getDriver().getFullName());
        return dto;
    }
}
