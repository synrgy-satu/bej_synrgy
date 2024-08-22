package com.example.finalProject_synrgy.service;

import java.security.Principal;
import java.util.UUID;

public interface NotificationService {
    public boolean saveNotification(String title, String desc, String belongsTo);

    public boolean sendNotification(UUID uuid);

    public boolean testSendNotification();

    public Object get(Principal principal, int size, int pagenum);
}
