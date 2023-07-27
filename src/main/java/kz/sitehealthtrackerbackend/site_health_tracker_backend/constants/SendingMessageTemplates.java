package kz.sitehealthtrackerbackend.site_health_tracker_backend.constants;

import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.Site;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.statuses.SiteGroupStatus;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.web.dtos.SiteDto;

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

    public static final String TELEGRAM_GREETING_TEXT_TEMPLATE = "Добро пожаловать!\n" +
            "Я - бот сервиса Site Health Tracker. " +
            "В мои обязанности входит оповещать подписчиков при изменении статусов групп.\n\n" +
            "Ваш аккаунт успешно добавлен в наш сервис. " +
            "Для получения уведомлений об изменениях статусов групп выберите подписаться в меню.";

    public static final String GROUP_STATUS_CHANGED_NOTIFICATION_MESSAGE_CONTENT_TEMPLATE = "Привет, подписчик!<d><d>" +
            "Уведомляем Вас, что статус группы %s изменился: %s.<d>" +
            "Причина: статус сайта %s изменился - %s.<d><d>" +
            "С уважением,<d>" +
            "Site Health Tracker team.";

    public static String groupStatusChangedTemplateWithDelimiter(SiteGroup siteGroup, Delimiters delimiter, SiteDto siteWithChangedStatus) {
        String groupName = siteGroup.getName();
        String newGroupStatus = siteGroup.getStatus().getStatusValue();
        String siteName = siteWithChangedStatus.getName();
        String siteStatus = siteWithChangedStatus.getStatus().getStatusValue();
        String delimitersReplacedTemplate = GROUP_STATUS_CHANGED_NOTIFICATION_MESSAGE_CONTENT_TEMPLATE
                .replaceAll(Delimiters.REPLACING_DELIMITER.DELIMITER, delimiter.DELIMITER);

        return String.format(delimitersReplacedTemplate, groupName, newGroupStatus, siteName, siteStatus);
    }
}
