package com.assignment_two_starter.model.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String email, String subject, String message, Boolean isHtml) {
        try {

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, isHtml);

            mailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
