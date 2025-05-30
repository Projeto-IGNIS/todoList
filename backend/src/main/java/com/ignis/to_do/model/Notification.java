package com.ignis.to_do.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class Notification {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @ManyToOne
    private Task task;
    private String message;      
    private boolean read = false;   

    public Notification( Task task, String message, boolean read, Long userId) {

        this.userId = userId;
        this.task = task;
        this.message = message;
        this.read = read;
        
    }

    public static Object builder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'builder'");
    }

}
 