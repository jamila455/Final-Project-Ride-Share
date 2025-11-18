package com.example.campusrideshare.service;

import com.example.campusrideshare.model.Ride;
import com.example.campusrideshare.model.RideSignup;
import com.example.campusrideshare.repository.RideSignupRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideEmailService {

    private final JavaMailSender mailSender;
    private final RideSignupRepository rideSignupRepository;

    // we can send generic notifications to this address (manager or system)
    private final String fromAddress = "no-reply@campusrideshare.local";

    public RideEmailService(JavaMailSender mailSender,
                            RideSignupRepository rideSignupRepository) {
        this.mailSender = mailSender;
        this.rideSignupRepository = rideSignupRepository;
    }

    public void sendPassengerSignupEmail(String toEmail, String passengerName, Ride ride) {
        String subject = "Ride Confirmation: " + ride.getFromLocation() + " → " + ride.getToCampus();
        String body = """
                Hi %s,
                
                You have been signed up for a ride:
                
                From: %s
                To: %s
                Departure: %s
                
                Thank you for using Campus Ride Share!
                """.formatted(
                passengerName,
                ride.getFromLocation(),
                ride.getToCampus(),
                ride.getDepartureTime()
        );

        sendEmail(toEmail, subject, body);
    }

    public void sendRideFullEmail(Ride ride) {
        // notify the driver for now
        String subject = "Ride is now full: " + ride.getFromLocation() + " → " + ride.getToCampus();
        String body = """
                Hi %s,
                
                Your ride departing %s from %s to %s is now FULL.
                
                Campus Ride Share
                """.formatted(
                ride.getDriver().getFullName(),
                ride.getDepartureTime(),
                ride.getFromLocation(),
                ride.getToCampus()
        );

        sendEmail(ride.getDriver().getEmail(), subject, body);
    }

    public void sendRideCancelledEmails(Ride ride) {
        // email all passengers who signed up
        List<RideSignup> signups = rideSignupRepository.findByRide(ride);

        for (RideSignup signup : signups) {
            String to = signup.getPassenger().getEmail();
            String subject = "Ride Cancelled: " + ride.getFromLocation() + " → " + ride.getToCampus();
            String body = """
                    Hi %s,
                    
                    Unfortunately, the ride you signed up for has been cancelled.
                    
                    From: %s
                    To: %s
                    Original departure: %s
                    
                    Please choose another ride in the Campus Ride Share app.
                    """.formatted(
                    signup.getPassenger().getFullName(),
                    ride.getFromLocation(),
                    ride.getToCampus(),
                    ride.getDepartureTime()
            );

            sendEmail(to, subject, body);
        }
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
        } catch (MessagingException e) {
            // for your project, logging the error is enough
            e.printStackTrace();
        }
    }
}
