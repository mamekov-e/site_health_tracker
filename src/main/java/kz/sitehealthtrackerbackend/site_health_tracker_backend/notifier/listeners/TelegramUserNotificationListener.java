package kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.listeners;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.ApplicationContextProvider;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.Delimiters;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.SendingMessageTemplates;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.TelegramUser;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.EventListener;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramUserNotificationListener implements EventListener {
    @Autowired
    private TelegramUserService telegramUserService;

    @Override
    public void update(SiteGroup siteGroup, Site siteWithChangedStatus) {
        List<TelegramUser> telegramUserList = telegramUserService.findAllTelegramUsersEnabledIs(true);

        for (TelegramUser telegramUser : telegramUserList) {
            String text = SendingMessageTemplates
                    .groupStatusChangedTemplateWithDelimiter(siteGroup, Delimiters.NEW_LINE, siteWithChangedStatus);
            long chatId = telegramUser.getId();
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
            Thread sendingThread = sendMessageInThread(sendMessage);
            sendingThread.start();
        }
    }

    private Thread sendMessageInThread(SendMessage sendMessage) {
        return new Thread(() -> {
            TelegramWebhookBot telegramWebhookBot = ApplicationContextProvider
                    .getApplicationContext().getBean(TelegramWebhookBot.class);
            try {
                telegramWebhookBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                System.out.println(e);
            }
        });
    }
}
