package com.MonitoringTool.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {

        System.out.println("📧 sendEmail() called");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("patilrushikesh1983@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    public void sendActivityCreatedMail(String to, String project, String activity) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("New Activity Assigned");
        message.setText(
            "You have been assigned a new activity.\n\n" +
            "Project: " + project + "\n" +
            "Activity: " + activity
        );

        mailSender.send(message);
    }

}
