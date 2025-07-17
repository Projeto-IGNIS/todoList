package com.ignis.to_do.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ignis.to_do.dto.NotificationDTO;
import com.ignis.to_do.model.Notification;
import com.ignis.to_do.repository.NotificationRepository;

@Service
public class NotificationService {
  
    private NotificationRepository notificationRepository;
   
    public Notification createNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    public List<NotificationDTO> getUnread() {
          return notificationRepository.findByReadFalse()
            .stream()
            .map(n -> new NotificationDTO(n.getId(), n.getMessage(), n.isRead()))
            .collect(Collectors.toList());
     }   


     public void markAsRead(Long id) {
           Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found"));
           notification.setRead(true);
           notificationRepository.save(notification);
     }


}
