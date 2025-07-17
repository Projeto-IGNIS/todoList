package com.ignis.to_do.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ignis.to_do.dto.NotificationDTO;
import com.ignis.to_do.model.Notification;
import com.ignis.to_do.service.NotificationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification Controller", description = "Gerenciamento de Notificações")
public class NotificationController {

    @Autowired
    private NotificationService service;


    @PostMapping("/createNotification")
    public Notification createNotification(@RequestBody String message) {
        return service.createNotification(message);
    }

    @GetMapping("/unread")
    public List<NotificationDTO> getUnreadNotifications() {
        return service.getUnread();
    }

    @PutMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
    }

}
