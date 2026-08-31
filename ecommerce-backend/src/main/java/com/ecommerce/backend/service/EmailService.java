package com.ecommerce.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(
            String recipientEmail,
            String verificationToken) {

        String verificationLink =
                "http://localhost:8080/api/auth/verify?token="
                + verificationToken;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(recipientEmail);

        message.setSubject(
                "E-Commerce Account Email Verification");

        message.setText(
                "Hello,\n\n"
                + "Thank you for creating an account.\n\n"
                + "Please click the link below to verify your email:\n\n"
                + verificationLink
                + "\n\n"
                + "If you did not create this account, "
                + "you can ignore this email.\n\n"
                + "Thank you."
        );

        mailSender.send(message);
    }
}