package kz.sitehealthtrackerbackend.site_health_tracker_backend.telegram.component;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.TelegramUser;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class MenuOptionComponent {

    @Autowired
    private TelegramUserService telegramUserService;

    public BotApiMethod<?> showMenuOptions(String text, long chatId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(chatId);

        return createMessageWithKeyboard(chatId, text, replyKeyboardMarkup);
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard(long chatId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        TelegramUser telegramUser = telegramUserService.findById(chatId);
        String text = telegramUser.isEnabled() ? "Отписаться" : "Подписаться";

        KeyboardRow enabledRow = new KeyboardRow();
        enabledRow.add(new KeyboardButton(text));
        enabledRow.add(new KeyboardButton("Помощь"));

        replyKeyboardMarkup.setKeyboard(List.of(enabledRow));
        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(long chatId, String textMessage, ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}
