package com.ignis.to_do.service;

import org.springframework.stereotype.Service;
//import com.ignis.to_do.dto.NotificationDTO;
import com.ignis.to_do.dto.TaskDTO;
import com.ignis.to_do.model.Notification;
import com.ignis.to_do.model.Task;
import com.ignis.to_do.model.User;

@Service
public class NotificationService {
   private TaskService taskService;
  // private NotificationDTO notificationDTO;

   public NotificationService(TaskService taskService) {
          this.taskService = taskService;
   }

   public String createNotification(Task task , User user){

          Notification notification = new Notification(user, task, "Task " + task.getTitle() + " is overdue", false);
          
          return notification.toString(); 

   }
   
   public void sendNotification(Notification notification, User user){
        //TO DO
   }

   public void getNotificationsByUser(User user){
         //TO DO
   }

   public void deleteNotification(Notification notification){
        //TO DO
   }

   public String getTasksToNotify(){
        
        Iterable<TaskDTO> allOverdueTasks = taskService.checkAllOverdueTasks();
        allOverdueTasks.forEach(task -> System.out.println(task.getTitle()));
        
        return allOverdueTasks.toString();

   }

   public String markNotificationAsRead(Notification notification){
        notification.setRead(true);
     return "Notification marked as read";
   }

}
