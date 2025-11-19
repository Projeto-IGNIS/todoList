package com.ignis.to_do.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ignis.to_do.dto.TaskListDTO;
import com.ignis.to_do.service.TaskListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/taskList")
@Tag(name = "TaskList Controller", description = "Gerenciamento de listas de tarefas")
public class TaskListController {
    
    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }
    
    @PostMapping("/createTaskList")
    public TaskListDTO createTaskList(
        @Valid @RequestBody TaskListDTO taskListDTO) {      

        return taskListService.createTaskList(taskListDTO);
    }

    @GetMapping("/{taskListId}")
    public TaskListDTO getTaskListById(@PathVariable Long taskListId) {

        return taskListService.getTaskListById(taskListId);
    }

    @GetMapping("/allTaskLists")
    public Iterable<TaskListDTO> getAllTaskLists() {

        return taskListService.getAllTaskLists();
    }
    
    @PutMapping("/updateTaskList")     
    public TaskListDTO updateTaskListTitle(
        @RequestBody TaskListDTO taskListDTO){

        return taskListService.updateTaskListTitle(taskListDTO);
    }

    //FAZ SENTIDO?
    @PutMapping("updateBoardId")
    public TaskListDTO updateBoardId(
        @RequestBody TaskListDTO taskListDTO) {
        
        return taskListService.updateBoardId(taskListDTO);
    }

    @DeleteMapping("/deleteTaskList/{taskListId}")
    public void deleteTaskListById(@PathVariable Long taskListId) {

        taskListService.deleteTaskListById(taskListId);
    }
}
