package kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.handler;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.TelegramUserService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.component.MenuOptionComponent;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.state.MenuOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

    @Autowired
    private TelegramUserService telegramUserService;
    @Autowired
    private EventHandler eventHandler;
    @Autowired
    private MenuOptionComponent menuOptionComponent;

    public BotApiMethod<?> handle(Message message, MenuOptions menuOption) {
        long chatId = message.getChatId();
        String username = message.getFrom().getUserName();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        boolean userExists = telegramUserService.existById(chatId);
        if (!userExists) {
            return eventHandler.addTelegramUser(chatId, username, sendMessage);
        }

        switch (menuOption) {
            case START -> {
                String botStartedText = "Бот уже запущен. Можете пользоваться системой, используйте меню.";
                return menuOptionComponent.showMenuOptions(botStartedText, chatId);
            }
            case SUBSCRIBE -> {
                return eventHandler.changeEnabledTo(chatId, true);
            }
            case UNSUBSCRIBE -> {
                return eventHandler.changeEnabledTo(chatId, false);
            }
            default -> {
                return eventHandler.helpMessage(sendMessage);
            }
        }
    }
}
