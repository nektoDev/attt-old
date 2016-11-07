package ru.nektodev.service.attt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nektodev.notification.api.NotificationFacade;

import java.util.List;

/**
 * @author nektodev
 * @date 07/11/2016
 */
@Service
public class NotificationService {
    @Autowired
    private NotificationFacade notificationFacade;

    @Value("${default.watcher}")
    private String DEFAULT_WATCHER;

    public void notify(List<String> watchers, String message) {
        if (watchers == null || watchers.isEmpty()) {
            DEFAULT_WATCHER = "slava";
            notificationFacade.sendMessage(DEFAULT_WATCHER, message);
        }
    }
}
