package kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.listeners;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.config.ApplicationContextProvider;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.Delimiters;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.SendingMessageTemplates;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.TelegramUser;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.EventListener;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.service.TelegramUserService;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;

import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.ExpireConstants.DELETE_UNSUBSCRIBED_TELEGRAM_USERS_CRON;

@Component
public class TelegramUserNotificationListener implements EventListener {
    @Autowired
    private TelegramUserService telegramUserService;

    @Transactional
    @Scheduled(cron = DELETE_UNSUBSCRIBED_TELEGRAM_USERS_CRON)
    public void checkVerificationCodeExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("Удаление всех отписавшихся от бота пользователей телеграм до " + currentTime);
        telegramUserService.deleteAllByEnabledFalseAndCreatedAtBefore(currentTime);
    }

    @Override
    public void update(SiteGroup siteGroup, SiteDto siteWithChangedStatus) {
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
