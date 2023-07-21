package kz.sitehealthtrackerbackend.site_health_tracker_backend.web.controller;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.SpringWebhookTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
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
