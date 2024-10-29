package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller.notification;

import jakarta.servlet.http.HttpServletRequest;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Email;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.EmailService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.listeners.EmailNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/emails")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class EmailNotifierController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailNotificationListener emailNotificationListener;

    @PostMapping("/register")
    public ResponseEntity<Long> registerEmailToNotifier(@RequestParam("email-address") String address, HttpServletRequest request) {
        String urlPath = request.getRequestURL().toString();
        Email registeredEmail = emailService.registerEmailToNotifier(address);
        emailNotificationListener.sendEmailVerification(registeredEmail, urlPath);
        return new ResponseEntity<>(registeredEmail.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/unregister")
    public ResponseEntity<String> unregisterEmailFromNotifier(@RequestParam("email-address") String address) {
        emailService.unregisterEmailFromNotifier(address);
        emailNotificationListener.sendUnregisteredMessage(address);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @GetMapping("/register/verify")
    public String verifyEmail(@RequestParam("code") String code) {
        boolean verified = emailService.verify(code);

        if (verified) {
            return "verification-success";
        }

        return "verification-fail";
    }

}
