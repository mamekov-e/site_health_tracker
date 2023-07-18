package kz.sitehealthtracker.site_health_tracker.service.impls;

import kz.sitehealthtracker.site_health_tracker.config.ApplicationContextProvider;
import kz.sitehealthtracker.site_health_tracker.constants.Delimiters;
import kz.sitehealthtracker.site_health_tracker.constants.SendingMessageTemplates;
import kz.sitehealthtracker.site_health_tracker.model.SiteGroup;
import kz.sitehealthtracker.site_health_tracker.model.TelegramUser;
import kz.sitehealthtracker.site_health_tracker.model.enums.SiteGroupStatus;
import kz.sitehealthtracker.site_health_tracker.service.TelegramBotNotifierService;
import kz.sitehealthtracker.site_health_tracker.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@EnableScheduling
@Service
public class TelegramBotNotifierServiceImpl implements TelegramBotNotifierService {

    @Autowired
    private TelegramUserService telegramUserService;

    @Override
    public void notifyTelegramUsers(SiteGroup siteGroup, SiteGroupStatus oldStatus) {
        List<TelegramUser> telegramUserList = telegramUserService.findAllTelegramUsersEnabledIs(true);

        for (TelegramUser telegramUser : telegramUserList) {
            String text = SendingMessageTemplates
                    .groupStatusChangedTemplateWithDelimiter(siteGroup, Delimiters.NEW_LINE);
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
