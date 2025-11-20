package com.ignis.to_do.service;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.ignis.to_do.dto.TaskDTO;
import com.ignis.to_do.exception.task_exception.TaskNotFoundException;
import com.ignis.to_do.model.Task;
import com.ignis.to_do.model.TaskList;
import com.ignis.to_do.repository.TaskRepository;
import com.ignis.to_do.validator.StatusValidator;

import jakarta.transaction.Transactional;

@Service
public class TaskService implements TaskReminder {

    private final TaskRepository taskRepository;
    private final TaskListService taskListService;
    private final CategoryService categoryService;

    private static final String TASK_NOT_FOUND = "Task com ID %s nao encontrado";

    public TaskService(TaskRepository taskRepository, TaskListService taskListService, CategoryService categoryService) {
        this.taskRepository = taskRepository;
        this.taskListService = taskListService;
        this.categoryService = categoryService;
    }

    public String createTask(TaskDTO taskDTO) {  

        StatusValidator taskStatus = new StatusValidator(taskDTO.getStatus().toString());
        if (taskStatus.validateStatus(taskDTO.getStatus().toString())) {            
            TaskList taskList = taskListService.getList(taskDTO.getListId());        
            Task task = new Task(taskDTO.getTitle(), taskList, taskDTO.getStatus());      
            task.setDescription(taskDTO.getDescription());
            if (taskDTO.getCategoryId() != null) {
                task.setCategory(categoryService.getCategoryById(taskDTO.getCategoryId()));
            }
            task.setDueDate(taskDTO.getDueDate());
            taskRepository.save(task);        
            return "Task criada com sucesso";
        }

        return "Status inválido";
    }    public TaskDTO getTaskById(Long taskId) {  
        Task task = taskRepository.findById(taskId).orElseThrow(()
         -> new TaskNotFoundException(TASK_NOT_FOUND.formatted(taskId))); 
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getList().getId(), task.getCategory() != null ? task.getCategory().getId() : null, task.getDueDate()); 
    }

    public void verifyIfTaskExists(Long taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND.formatted(taskId)));
    }

    public Iterable<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getList().getId(), task.getCategory() != null ? task.getCategory().getId() : null, task.getDueDate()))
            .toList();
    }
    
    public void deleteTaskById(Long taskId) { 
        verifyIfTaskExists(taskId);       
        taskRepository.deleteById(taskId);     
    }

    @Transactional
    public TaskDTO updateTaskTitle(TaskDTO taskDTO) {

        taskRepository.updateTaskTitle(taskDTO.getId(), taskDTO.getTitle());
        return getTaskById(taskDTO.getId());
    }

    @Transactional
    public TaskDTO updateTaskList(Long taskId, Long listId) {
        verifyIfTaskExists(taskId);
        taskListService.verifyIfTaskListExists(listId);
        taskRepository.updateTaskList(taskId, listId);
        return getTaskById(taskId);
    }
    
    @Override
    public String checkOverdueTasks(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
            () -> new TaskNotFoundException(TASK_NOT_FOUND.formatted(taskId)));

        LocalDate today = LocalDate.now();
        LocalDate taskDueDate = task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (taskDueDate.isBefore(today)) {
            sendTaskReminder();
            return "Lembrete de tarefa enviado.";
        }

        return "A tarefa ainda nao esta atrasada.";
    }

    @Override
    public void sendTaskReminder() {
        // Implementation for sending reminder
    }
}
