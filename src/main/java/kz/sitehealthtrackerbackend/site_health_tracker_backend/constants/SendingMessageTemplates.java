package kz.sitehealthtrackerbackend.site_health_tracker_backend.constants;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;

import java.util.List;

import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.Delimiters.NEW_LINE;
import static kz.sitehealthtrackerbackend.site_health_tracker_backend.constants.ExpireConstants.TELEGRAM_UNSUBSCRIBED_EXPIRATION_TIME;

public class SendingMessageTemplates {
    public static final String EMAIL_VERIFICATION_MESSAGE_CONTENT_TEMPLATE = "Привествуем Вас!<br>" +
            "Это сообщение было отправлено Вам для подтверждения подписки на рассылки сервиса Site Health Tracker!<br>" +
            "Пожалуйста подтвердите подписку, перейдя по ссылке ниже:<br>" +
            "<h4><a href=\"%s\">Подтвердить подписку</a></h4><br>" +
            "Срок действия потверждения истечет через 1 час.<br><br>" +
            "В случае если Вы получили это сообщение случайно, пожалуйста, проигнорируйте. Просим извинения за беспокойcтво!<br>" +
            "С уважением,<br>" +
            "Site Health Tracker team.";

    public static final String UNREGISTER_MESSAGE_CONTENT_TEMPLATE = "Привествуем Вас!<br>" +
            "Отписка от рассылки сервиса Site Health Tracker прошла успешно!<br><br>" +
            "С уважением,<br>" +
            "Site Health Tracker team.";

    public static final String TELEGRAM_GREETING_TEXT_TEMPLATE = String.format("Добро пожаловать!\n" +
            "Я - бот сервиса Site Health Tracker. " +
            "В мои обязанности входит оповещать подписчиков при изменении статусов групп.\n\n" +
            "Ваш аккаунт успешно добавлен в наш сервис. " +
            "Для получения уведомлений об изменениях статусов групп выберите подписаться в меню.\n\n" +
            "Если вы отпишитесь от подписки, ваш аккаунт будет удален из базы через %s день после отписки.", TELEGRAM_UNSUBSCRIBED_EXPIRATION_TIME.toDays());

    public static final String TELEGRAM_HELP_TEXT_TEMPLATE = String.format("Доступные опции меню (можно ввести номер):\n" +
            "1. Подписаться\n" +
            "2. Отписаться\n" +
            "3. Помощь\n\n" +
            "Обратите внимание! Если вы отписались от подписки, ваш аккаунт будет удален из базы через %s день после отписки.", TELEGRAM_UNSUBSCRIBED_EXPIRATION_TIME.toDays());

    public static final String GROUP_STATUS_CHANGED_NOTIFICATION_MESSAGE_CONTENT_TEMPLATE = "Привет, подписчик!<d><d>" +
            "Уведомляем Вас, что статус группы %s изменился: %s.<d>" +
            "Причина: %s - %s.<d><d>" +
            "Все сайты группы:<d>" +
            "%s<d>" +
            "С уважением,<d>" +
            "Site Health Tracker team.";

    private static final String SITE_ITERATION_TEXT = "%d. %s - %s";

    public static String groupStatusChangedTemplateWithDelimiter(SiteGroup siteGroup, Delimiters delimiter, SiteDto siteWithChangedStatus) {
        String groupName = siteGroup.getName();
        String newGroupStatus = siteGroup.getStatus().getStatusValue();
        String siteName;
        String siteStatus;
        if (siteWithChangedStatus.getName() != null) {
            siteName = "сайт " + siteWithChangedStatus.getName();
            siteStatus = siteWithChangedStatus.getStatus().getStatusValue();
        } else {
            siteName = "все сайты ";
            siteStatus = "удалены";
        }
        List<Site> sitesOfGroup = siteGroup.getSites();
        StringBuilder allGroupSites = new StringBuilder();

        if (sitesOfGroup.isEmpty()) {
            allGroupSites.append("Нет сайтов").append(NEW_LINE.getDELIMITER());
        } else {
            for (int i = 0; i < sitesOfGroup.size(); i++) {
                Site site = sitesOfGroup.get(i);
                String siteText = String.format(SITE_ITERATION_TEXT, i+1, site.getName(), site.getStatus().getStatusValue());
                allGroupSites.append(siteText).append(NEW_LINE.getDELIMITER());
            }
        }
        String delimitersReplacedTemplate = GROUP_STATUS_CHANGED_NOTIFICATION_MESSAGE_CONTENT_TEMPLATE
                .replaceAll(Delimiters.REPLACING_DELIMITER.DELIMITER, delimiter.DELIMITER);

        return String.format(delimitersReplacedTemplate, groupName, newGroupStatus, siteName, siteStatus, allGroupSites);
    }
}
