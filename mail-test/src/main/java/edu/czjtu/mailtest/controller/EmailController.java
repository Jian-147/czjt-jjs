package edu.czjtu.mailtest.controller;


import edu.czjtu.mailtest.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @GetMapping("/send")
    public String sendTestEmail(@RequestParam String to, @RequestParam String
            subject, @RequestParam String text) {
        try {
            emailService.sendEmail(to, subject, text);
            return "Email sent successfully";
        } catch (MessagingException e) {
            return "Failed to send email: " + e.getMessage();
        }
    }
}