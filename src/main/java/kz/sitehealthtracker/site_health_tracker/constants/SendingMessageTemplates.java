package kz.sitehealthtracker.site_health_tracker.constants;

public class SendingMessageTemplates {
    public static final String VERIFICATION_MESSAGE_CONTENT_TEMPLATE = "Dear, subscriber to Site Health Tracker mailing!<br>" +
            "Please, verify your registration by clicking link below:" +
            "<h4><a href=\"%s\">Click me to verify</a></h4><br><br>" +
            "Best regards,<br>" +
            "Site Health Tracker team.";
    public static final String GROUP_STATUS_CHANGED_NOTIFICATION_MESSAGE_CONTENT_TEMPLATE = "Hi, subscriber!<br>" +
            "We are notifying you that group status of %s changed from %s to %s.<br><br>" +
            "Best regards,<br>" +
            "Site Health Tracker team.";
}
