package com.ignis.to_do.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            
    private String title;
    private String description;
    @Column(name = "status")
    private String status;
    @ManyToOne
    @JoinColumn(name = "list_id")
    private TaskList list;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Date dueDate;

    public Task(String title, TaskList list, String description, String status, Date dueDate) {
        this.title = title;
        this.list = list;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate; // Set default due date to current date
    }

    public Task createTask(Task task) {  
        task.setTitle(task.getTitle());
        task.setDescription(task.getDescription());
        task.setStatus(task.getStatus());
        task.setList(task.getList());        
        task.setCategory(task.getCategory());
        task.setDueDate(task.getDueDate());
        return task;      
        
    } 
    

    public void deleteTask(Task task) {      

        this.list.removeTask(task);
    }

    public void markAsComplete() {
        // TO DO
    } 
    
    public void assignCategory(Category category) {
        //this.category = category;
        this.setCategory(category);
    }

    public void isOverdue(){
        // TO DO
    }
}