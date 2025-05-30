package com.ignis.to_do.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ignis.to_do.dto.NotificationDTO;
import com.ignis.to_do.model.Notification;
import com.ignis.to_do.service.NotificationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService = new NotificationService();

    @GetMapping("/{userId}")
    public List<Notification> geNotifications(@PathVariable Long userId) {
        return notificationService.getNotifications(userId);
    }

    public String getNotifications() {
       
        return notificationService.getTasksToNotify();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createNotification(@RequestBody NotificationDTO dto) {
        notificationService.createNotification(dto.getUserId(), dto.getMessage(), dto.getTaskId());
        return ResponseEntity.ok().build();
    }
/* 
    @PostMapping("/markAsRead/{notificationId}")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    @PostMapping("/markAllAsRead/{userId}")
    public void markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
    }
*/
    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

}
