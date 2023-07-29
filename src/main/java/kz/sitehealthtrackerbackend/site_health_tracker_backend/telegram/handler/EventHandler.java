package kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.handler;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.SendingMessageTemplates;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.TelegramUser;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.TelegramUserService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.component.MenuOptionComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class EventHandler {

    @Autowired
    private TelegramUserService telegramUserService;
    @Autowired
    private MenuOptionComponent menuOptionComponent;

    public SendMessage addTelegramUser(long chatId, String username, SendMessage sendMessage) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setId(chatId);
        telegramUser.setUsername(username);
        telegramUser.setEnabled(false);

        telegramUserService.saveTelegramUser(telegramUser);

        sendMessage.setText(SendingMessageTemplates.TELEGRAM_GREETING_TEXT_TEMPLATE);

        return sendMessage;
    }

    public SendMessage helpMessage(SendMessage sendMessage) {
        sendMessage.setText(SendingMessageTemplates.TELEGRAM_HELP_TEXT_TEMPLATE);
        return sendMessage;
    }

    public BotApiMethod<?> changeEnabledTo(long chatId, boolean enabled) {
        TelegramUser telegramUser = telegramUserService.getById(chatId);
        if (telegramUser.isEnabled() != enabled) {
            telegramUser.setEnabled(enabled);
            telegramUserService.saveTelegramUser(telegramUser);

            String statusChangedText = enabled ? "Вы успешно подписались на рассылку" :
                    "Вы успешно отписались от рассылки";
            return menuOptionComponent.showMenuOptions(statusChangedText, chatId);
        } else {
            String statusAlreadyChangedText = enabled ? "Вы уже подписаны на рассылку" :
                    "Вы уже отписаны от рассылки";
            return menuOptionComponent.showMenuOptions(statusAlreadyChangedText, chatId);
        }
    }
}
