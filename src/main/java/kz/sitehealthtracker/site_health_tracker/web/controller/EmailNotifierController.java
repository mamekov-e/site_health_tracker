package kz.sitehealthtracker.site_health_tracker.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import kz.sitehealthtracker.site_health_tracker.model.Email;
import kz.sitehealthtracker.site_health_tracker.service.EmailService;
import kz.sitehealthtracker.site_health_tracker.notifier.listeners.EmailNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/emails")
public class EmailNotifierController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailNotificationListener emailNotificationListener;

    @PostMapping
    public ResponseEntity<Long> registerEmailToNotifier(@RequestParam("email-address") String address, HttpServletRequest request) {
        String urlPath = request.getRequestURL().toString();
        Email registeredEmail = emailService.registerEmailToNotifier(address);
        emailNotificationListener.sendEmailVerification(registeredEmail, urlPath);
        return new ResponseEntity<>(registeredEmail.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> unregisterEmailFromNotifier(@RequestParam("email-address") String address) {
        emailService.unregisterEmailFromNotifier(address);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("code") String code) {
        boolean verified = emailService.verify(code);

        if (verified) {
            return "verification-success";
        }

        return "verification-fail";
    }

}
