package com.fintech.banking.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Verify your email");

            String verificationLink =
                    "http://localhost:8899/api/users/verify?token=" + token;

            String htmlContent = """
                    <h3>Welcome to Fintech Banking</h3>
                    <p>Please verify your email by clicking below:</p>
                    <a href="%s">Verify Email</a>
                    """.formatted(verificationLink);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password");

            String resetLink =
                    "http://localhost:8899/api/users/reset-password?token=" + token;

            String htmlContent = """
                <h3>Password Reset Request</h3>
                <p>Click the link below to reset your password:</p>
                <a href="%s">Reset Password</a>
                <p>If you did not request this, ignore this email.</p>
                """.formatted(resetLink);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send reset email", e);
        }
    }
}
