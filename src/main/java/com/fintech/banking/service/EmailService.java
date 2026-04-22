package com.fintech.banking.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private void sendHtmlEmail(String to, String subject, String htmlContent) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom(fromEmail, "Fintech Banking");
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

            log.info("Email sent to {}", to);

        } catch (Exception e) {
            log.error("Email sending failed to {}", to, e);
            throw new RuntimeException("Failed to send email");
        }
    }

    public void sendVerificationEmail(String toEmail, String token) {

        String link = baseUrl + "/api/users/verify?token=" + token;

        String html = """
                <div style="font-family: Arial; padding:20px;">
                    <h2>Welcome to Fintech Banking</h2>
                    <p>Please verify your email to activate your account.</p>
                    <a href="%s" style="
                        background-color:#4CAF50;
                        color:white;
                        padding:10px 20px;
                        text-decoration:none;
                        border-radius:5px;">
                        Verify Email
                    </a>
                    <p>If you didn’t create this account, ignore this email.</p>
                </div>
                """.formatted(link);

        sendHtmlEmail(toEmail, "Verify your email", html);
    }

    public void sendPasswordResetEmail(String toEmail, String token) {

        String link = baseUrl + "/api/users/reset-password?token=" + token;

        String html = """
                <div style="font-family: Arial; padding:20px;">
                    <h2>Password Reset</h2>
                    <p>You requested to reset your password.</p>
                    <a href="%s" style="
                        background-color:#f44336;
                        color:white;
                        padding:10px 20px;
                        text-decoration:none;
                        border-radius:5px;">
                        Reset Password
                    </a>
                    <p>If you didn’t request this, ignore this email.</p>
                </div>
                """.formatted(link);

        sendHtmlEmail(toEmail, "Reset your password", html);
    }
}