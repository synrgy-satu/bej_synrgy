package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.entity.Notification;
import com.example.finalProject_synrgy.repository.NotificationRepository;
import com.example.finalProject_synrgy.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    private PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public boolean saveNotification(String title, String desc, String belongsTo) {
        Notification notification = new Notification();

        notification.setTitle(title);
        notification.setDesc(desc);
        notification.setBelongsTo(belongsTo);
        try {
            notificationRepository.save(notification);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public boolean sendNotification(UUID uuid) {
//        Optional<Notification> notification = notificationRepository.findById(uuid);
//        simpMessagingTemplate.convertAndSendToUser();
        return true;
    }

    public boolean testSendNotification() {
//        Optional<Notification> notification = notificationRepository.findById(uuid);
        simpMessagingTemplate.convertAndSendToUser("test", "/queue/notification", "Halo");
        return true;
    }

    public Object get(Principal principal, int size, int pagenum) {
        Page<Notification> notifications = notificationRepository
                .findByBelongsToIn(new String[]{principal.getName(), "all"},
                        PageRequest.of(pagenum, size));

        return notifications;
    }
}
