package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller.notification;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Email;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.listeners.EmailNotificationListener;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/emails")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class EmailNotifierController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailNotificationListener emailNotificationListener;

    @PostMapping("/register")
    public ResponseEntity<Long> registerEmailToNotifier(@RequestParam("email-address") String address) {
        Email registeredEmail = emailService.registerEmailToNotifier(address);
        emailNotificationListener.sendEmailVerification(registeredEmail);
        return new ResponseEntity<>(registeredEmail.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/unregister")
    public ResponseEntity<String> unregisterEmailFromNotifier(@RequestParam("email-address") String address) {
        emailService.unregisterEmailFromNotifier(address);
        emailNotificationListener.sendUnregisteredMessage(address);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

}
