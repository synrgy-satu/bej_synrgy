package com.example.finalProject_synrgy.service;

import java.security.Principal;

public interface NotificationService {
    public boolean saveNotification(String title, String desc, String belongsTo);

    public Object get(Principal principal, int size, int pagenum);
}
