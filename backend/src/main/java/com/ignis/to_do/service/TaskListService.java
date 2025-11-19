package com.ignis.to_do.service;

import org.springframework.stereotype.Service;
import com.ignis.to_do.dto.TaskListDTO;
import com.ignis.to_do.exception.task_list_exception.TaskListAlreadyExistsException;
import com.ignis.to_do.exception.task_list_exception.TaskListNotFoundException;
import com.ignis.to_do.model.Board;
import com.ignis.to_do.model.TaskList;
import com.ignis.to_do.repository.TaskListRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskListService {

    private final TaskListRepository taskListRepository;
    private final BoardService boardService;

    private static final String TASK_LIST_NOT_FOUND = "TaskList com ID %s nao encontrado";
    private static final String TASK_LIST_ALREADY_EXISTS = "TaskList com nome %s já existe";

    public TaskListService(TaskListRepository taskListRepository, BoardService boardService) {
        this.taskListRepository = taskListRepository;
        this.boardService = boardService;
    }
    
    public TaskListDTO createTaskList(TaskListDTO taskListDTO) {
        if (taskListRepository.findByName(taskListDTO.getTitle()).isPresent()) {
            throw new TaskListAlreadyExistsException(TASK_LIST_ALREADY_EXISTS.formatted(taskListDTO.getTitle()));
        }

        Board board = boardService.getBoard(taskListDTO.getBoardId());
        TaskList taskList = new TaskList(taskListDTO.getTitle(), board);
        return new TaskListDTO(taskListRepository.save(taskList).getId(), taskList.getName(), 
            taskList.getBoard().getId());
    }

    public TaskListDTO getTaskListById(Long taskLitsId) {
        return taskListRepository.findById(taskLitsId)
                .map(taskList -> new TaskListDTO(taskList.getId(), taskList.getName(), taskList.getBoard().getId()))
                .orElseThrow(() -> new TaskListNotFoundException(TASK_LIST_NOT_FOUND.formatted(taskLitsId)));
    }

    public void verifyIfTaskListExists(Long taskListId) {
        taskListRepository.findById(taskListId).orElseThrow(() -> 
        new TaskListNotFoundException(TASK_LIST_NOT_FOUND.formatted(taskListId)));
    }

    public Iterable<TaskListDTO> getAllTaskLists() {
        return taskListRepository.findAll().stream().map(taskList -> new TaskListDTO(
            taskList.getId(), taskList.getName(), taskList.getBoard().getId())).toList();
    }

    public TaskList getList(Long taskListId) {
        return taskListRepository.findById(taskListId).orElseThrow(() -> 
        new TaskListNotFoundException(TASK_LIST_NOT_FOUND.formatted(taskListId)));        
    }

    public void deleteTaskList(TaskList taskList) {     
        verifyIfTaskListExists(taskList.getId());   
        taskListRepository.delete(taskList);        
    }

    public void deleteTaskListById(Long taskListId) {
        verifyIfTaskListExists(taskListId);   
        taskListRepository.deleteById(taskListId);
    }

    @Transactional
    public TaskListDTO updateTaskListTitle(TaskListDTO taskListDTO) {

        taskListRepository.updateTaskListTitle(taskListDTO.getId(), taskListDTO.getTitle());
        return new TaskListDTO(taskListDTO.getId(), taskListDTO.getTitle(), 
                                taskListRepository.findById(taskListDTO.getId()).orElseThrow(() -> 
                                new TaskListNotFoundException(TASK_LIST_NOT_FOUND.formatted(taskListDTO.getId()))).getBoard().getId());
    }

    @Transactional
    public TaskListDTO updateBoardId(TaskListDTO taskListDTO) {
        verifyIfTaskListExists(taskListDTO.getId());
        taskListRepository.updateBoardId(taskListDTO.getId(), taskListDTO.getBoardId());
        return taskListDTO;
    }

}
