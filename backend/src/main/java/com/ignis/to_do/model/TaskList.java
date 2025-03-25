package com.ignis.to_do.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "task_list")
@Data
@AllArgsConstructor
@NoArgsConstructor  
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; 
    @ManyToOne
    @JoinColumn(name = "board_id")       
    private Board board;
    @OneToMany(mappedBy = "list")
    private List<Task> tasks;

    public TaskList(String name, Board board) {
        this.name = name;
        this.board = board;
    }

    public void addTask(Task task) {
        tasks.add(task); 
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }
}