package com.ignis.to_do.service;

public interface TaskReminder {
    
    public Boolean checkOverdueTasks(Long taskId);
    public String sendTaskReminder();


}
