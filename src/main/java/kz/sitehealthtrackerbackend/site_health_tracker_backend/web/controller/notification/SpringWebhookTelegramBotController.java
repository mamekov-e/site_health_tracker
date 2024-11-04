package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller.notification;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.SpringWebhookTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v1/public")
public class SpringWebhookTelegramBotController {

    @Autowired
    private SpringWebhookTelegramBot springWebhookTelegramBot;

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return springWebhookTelegramBot.onWebhookUpdateReceived(update);
    }

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Ok");
    }
}
