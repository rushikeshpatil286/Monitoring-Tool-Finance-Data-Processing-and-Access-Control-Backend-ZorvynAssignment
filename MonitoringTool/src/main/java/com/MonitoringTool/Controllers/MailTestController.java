package com.MonitoringTool.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MonitoringTool.Services.EmailService;

@RestController
@RequestMapping("/test")
public class MailTestController {

    private final EmailService emailService;

    public MailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/mail")
    public String testMail() {
        emailService.sendEmail(
            "patilrushikesh1983@gmail.com",
            "Test Mail",
            "Mail service is working"
        );
        return "Mail sent";
    }
}
