package com.ignis.to_do.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ignis.to_do.dto.TaskDTO;
import com.ignis.to_do.service.TaskService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/task")
@Tag(name = "Task Controller", description = "Gerenciamento de tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/createTask")
    public void createTask(@Valid @RequestBody TaskDTO taskDTO) {
        taskService.createTask(taskDTO);
        
    }

    @GetMapping("/getTask/{taskId}")
    public TaskDTO getTaskById(@PathVariable Long taskId) {

        return taskService.getTaskById(taskId);
    }   

    @GetMapping("/getAllTasks")
    public Iterable<TaskDTO> getAllTasks() {

        return taskService.getAllTasks();
    }  
    
    @GetMapping("/taskByTaskListId/{taskListId}")
    public Iterable<TaskDTO> getTasksByTaskListId(@PathVariable Long taskListId) {
        
        return taskService.getTasksByTaskListId(taskListId);
    }
    
    @PutMapping("/updateTaskTitle")
    public TaskDTO updateTaskTitle(
        @RequestBody TaskDTO taskDTO) {
            
        return taskService.updateTaskTitle(taskDTO);
    }

    @DeleteMapping("/deleteTask/{taskId}")
    public void deleteTaskById(@PathVariable Long taskId) {
        
        taskService.deleteTaskById(taskId); 
    }

    @GetMapping("/isOverdue/{taskId}")
    public Boolean checkOverdueTasks(@PathVariable Long taskId) {
        return taskService.checkOverdueTasks(taskId); 
    }

    @GetMapping("/checkAllOverdueTasks")
    public Iterable<TaskDTO> checkAllOverdueTasks() {
       return taskService.checkAllOverdueTasks(); 
      
    }



}   
