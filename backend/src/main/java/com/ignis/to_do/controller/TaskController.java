package com.ignis.to_do.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        String response = taskService.createTask(taskDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getTask/{taskId}")
    public TaskDTO getTaskById(@PathVariable Long taskId) {

        return taskService.getTaskById(taskId);
    }   

    @GetMapping("/getAllTasks")
    public Iterable<TaskDTO> getAllTasks() {

        return taskService.getAllTasks();
    }   
    
    @PutMapping("/updateTaskTitle")
    public TaskDTO updateTaskTitle(
        @RequestBody TaskDTO taskDTO) {
            
        return taskService.updateTaskTitle(taskDTO);
    }

    @PutMapping("/updateTaskList/{taskId}/{listId}")
    public TaskDTO updateTaskList(@PathVariable Long taskId, @PathVariable Long listId) {
        return taskService.updateTaskList(taskId, listId);
    }

    @DeleteMapping("/deleteTask/{taskId}")
    public void deleteTaskById(@PathVariable Long taskId) {
        
        taskService.deleteTaskById(taskId); 
    }

    @GetMapping("/checkOverdueTasks/{taskId}")
    public String checkOverdueTasks(@PathVariable Long taskId) {
        return taskService.checkOverdueTasks(taskId); 
    }
}   
