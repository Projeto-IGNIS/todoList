package com.ignis.to_do.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ignis.to_do.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping("/getNotifications")
    public String getNotifications() {
       
        return notificationService.getTasksToNotify();
    }

}
