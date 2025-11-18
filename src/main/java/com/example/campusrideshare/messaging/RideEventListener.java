package com.example.campusrideshare.messaging;

import com.example.campusrideshare.model.Ride;
import com.example.campusrideshare.repository.RideRepository;
import com.example.campusrideshare.service.RideEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RideEventListener {

    private final RideRepository rideRepository;
    private final RideEmailService rideEmailService;

    public RideEventListener(RideRepository rideRepository,
                             RideEmailService rideEmailService) {
        this.rideRepository = rideRepository;
        this.rideEmailService = rideEmailService;
    }

    @RabbitListener(queues = MessagingConfig.RIDE_EVENT_QUEUE)
    public void handleRideEvent(RideEventMessage message) {
        // Load the ride just once if we need it
        Ride ride = null;
        if (message.getRideId() != null) {
            ride = rideRepository.findById(message.getRideId()).orElse(null);
        }

        if ("PASSENGER_SIGNUP".equals(message.getType()) && ride != null) {
            rideEmailService.sendPassengerSignupEmail(
                    message.getPassengerEmail(),
                    message.getPassengerName(),
                    ride
            );
        }

        if ("RIDE_FULL".equals(message.getType()) && ride != null) {
            rideEmailService.sendRideFullEmail(ride);
        }

        if ("RIDE_CANCELLED".equals(message.getType()) && ride != null) {
            rideEmailService.sendRideCancelledEmails(ride);
        }
    }
}
