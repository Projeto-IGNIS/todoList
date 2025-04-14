package com.ignis.to_do.service;

import java.time.LocalDate;
import java.time.ZoneId;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

=======
import java.util.stream.StreamSupport;
>>>>>>> f792b8ef9935a18b7cf3675e3666045444f1856b
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

    private static final String TASK_NOT_FOUND = "Task com ID %s nao encontrado";

    public TaskService(TaskRepository taskRepository, TaskListService taskListService) {
        this.taskRepository = taskRepository;
        this.taskListService = taskListService;
    }

    public String createTask(TaskDTO taskDTO) {  

        StatusValidator taskStatus = new StatusValidator(taskDTO.getStatus());
        if (taskStatus.validateStatus(taskDTO.getStatus())) {            
            TaskList taskList = taskListService.getList(taskDTO.getListId());        
            Task task = new Task(taskDTO.getTitle(), taskList, taskDTO.getDescription(), taskDTO.getStatus(), taskDTO.getDueDate());      
            taskRepository.save(task);        
            return "Task criada com sucesso";
        }

        return "Status inválido";
    }

    public TaskDTO getTaskById(Long taskId) {  
        Task task = taskRepository.findById(taskId).orElseThrow(()
         -> new TaskNotFoundException(TASK_NOT_FOUND.formatted(taskId))); 
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getDueDate(), task.getList().getId()); 
    }

    public void verifyIfTaskExists(Long taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND.formatted(taskId)));
    }

    public Iterable<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(task -> new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getDueDate(), task.getList().getId()))
            .toList();
    }
    
    public Iterable<TaskDTO> getTasksByTaskListId(Long taskListId) {
        TaskList taskList = taskListService.getList(taskListId);
        return StreamSupport.stream(taskList.getTasks().spliterator(), false)
            .map(task -> new TaskDTO(task.getId(), task.getTitle(),  task.getStatus(), task.getDescription(), task.getDueDate(), task.getList().getId()))
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
    
    @Override
    public Boolean checkOverdueTasks(Long taskId) {
<<<<<<< HEAD
        
        verifyIfTaskExists(taskId);
        Task task = taskRepository.findById(taskId).orElseThrow(()
         -> new TaskNotFoundException(TASK_NOT_FOUND.formatted(taskId)));
=======
        Task task = taskRepository.findById(taskId).orElseThrow(
            () -> new TaskNotFoundException(TASK_NOT_FOUND.formatted(taskId)));

>>>>>>> 8209d4ed1e44eddc5f8ef552f03c3e958d8289ba
        LocalDate today = LocalDate.now();
        LocalDate taskDueDate = task.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (taskDueDate.isBefore(today)) {
            sendTaskReminder();
            return true;
        } 
        
        return false;
 
    }

    @Override
<<<<<<< HEAD
    public void sendTaskReminder() {
<<<<<<< HEAD
    
=======
    public String sendTaskReminder() {
        
        return "Task is overdue";
>>>>>>> 8209d4ed1e44eddc5f8ef552f03c3e958d8289ba
    }

    public Iterable<TaskDTO> checkAllOverdueTasks() {
        
        Iterable<TaskDTO> allTasks = getAllTasks();
        List<TaskDTO> allOverdueTasks = new ArrayList<>();

        for (TaskDTO task : allTasks) {
            
            if (checkOverdueTasks(task.getId()).equals(true)) {        
                allOverdueTasks.add(task);

            }

        }

       return allOverdueTasks;
<<<<<<< HEAD
=======
    // TODO
>>>>>>> f792b8ef9935a18b7cf3675e3666045444f1856b
=======
>>>>>>> 8209d4ed1e44eddc5f8ef552f03c3e958d8289ba
    }
}
