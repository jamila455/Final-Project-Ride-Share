package com.example.campusrideshare.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String RIDE_EVENT_QUEUE = "ride-events-queue";
    public static final String RIDE_EVENT_EXCHANGE = "ride-events-exchange";
    public static final String RIDE_EVENT_ROUTING_KEY = "ride.events";

    @Bean
    public Queue rideEventsQueue() {
        return new Queue(RIDE_EVENT_QUEUE, true);
    }

    @Bean
    public TopicExchange rideEventsExchange() {
        return new TopicExchange(RIDE_EVENT_EXCHANGE);
    }

    @Bean
    public Binding rideEventsBinding(Queue rideEventsQueue,
                                     TopicExchange rideEventsExchange) {
        return BindingBuilder
                .bind(rideEventsQueue)
                .to(rideEventsExchange)
                .with(RIDE_EVENT_ROUTING_KEY);
    }
}
