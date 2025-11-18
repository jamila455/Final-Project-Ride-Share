package com.example.campusrideshare.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.example.campusrideshare.messaging.MessagingConfig.*;

@Service
public class RideEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RideEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEvent(RideEventMessage message) {
        rabbitTemplate.convertAndSend(
                RIDE_EVENT_EXCHANGE,
                RIDE_EVENT_ROUTING_KEY,
                message
        );
    }
}
