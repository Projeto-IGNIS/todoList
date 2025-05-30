package com.ignis.to_do.service;

import java.util.List;

import org.springframework.stereotype.Service;
//import com.ignis.to_do.dto.NotificationDTO;
import com.ignis.to_do.dto.TaskDTO;
import com.ignis.to_do.model.Notification;
import com.ignis.to_do.model.Task;
import com.ignis.to_do.model.User;
import com.ignis.to_do.repository.NotificationRepository;
import com.ignis.to_do.repository.TaskRepository;

@Service
public class NotificationService {
  
    private final NotificationRepository notificationRepository = null;
    private final TaskRepository taskRepository = null;
    private final TaskService taskService = null;

   public String createNotification(Long userId, String message, Long taskId){
          Task task = taskRepository.findById(taskId)
               .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
          Notification notification = Notification.builder()
                .message(message)
                .read(false)
                .userId(userId)
                .task(task)
                .build();

        notificationRepository.save(notification);

   }
   
   public void sendNotification(Long userId, Long taskId) {
        //
   }

   public void getNotificationsByUser(Long userId){
         return;
   }

   public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
   }

   public void deleteNotification(Long notificationId){
        notificationRepository.deleteById(notificationId);
   }

   public String getTasksToNotify(){
        
        Iterable<TaskDTO> allOverdueTasks = taskService.checkAllOverdueTasks();
        allOverdueTasks.forEach(task -> System.out.println(task.getTitle()));
        
        return allOverdueTasks.toString();

   }

   public void markNotificationAsRead(Long notificationId) {
          Notification notification = notificationRepository.findById(notificationId)
               .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
          notification.setRead(true);
          notificationRepository.save(notification);
   }

}
