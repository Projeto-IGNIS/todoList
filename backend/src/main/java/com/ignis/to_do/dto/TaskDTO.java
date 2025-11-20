package com.ignis.to_do.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.ignis.to_do.model.TaskStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long listId;
    private Long categoryId;
    private Date dueDate;
}
