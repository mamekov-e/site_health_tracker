package kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier;

import jakarta.annotation.PostConstruct;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.model.SiteGroup;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.listeners.EmailNotificationListener;
import kz.sitehealthtrackerbackend.site_health_tracker_backend.notifier.listeners.TelegramUserNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventNotifier {

    @Autowired
    private EmailNotificationListener emailNotificationListener;
    @Autowired
    private TelegramUserNotificationListener telegramUserNotificationListener;

    private final List<EventListener> eventListeners = new ArrayList<>();

    @PostConstruct
    public void initSubscribers() {
        this.subscribe(emailNotificationListener);
        this.subscribe(telegramUserNotificationListener);
    }

    public void subscribe(EventListener service) {
        eventListeners.add(service);
    }

    public void unsubscribe(EventListener service) {
        eventListeners.remove(service);
    }

    public void notifyAll(SiteGroup siteGroup) {
        for (EventListener service : eventListeners) {
            service.update(siteGroup);
        }
    }
}
