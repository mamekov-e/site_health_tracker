package kz.sitehealthtracker.site_health_tracker.telegram;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
public class SpringWebhookTelegramBot extends SpringWebhookBot {

    private String botUsername;
    private String botPath;

    private TelegramFacade telegramFacade;

    public SpringWebhookTelegramBot(TelegramFacade telegramFacade, SetWebhook setWebhook, String botToken) {
        super(setWebhook, botToken);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
