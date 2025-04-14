package com.ignis.to_do.dto;

import lombok.Data;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String status;
    private String description;
    private Date dueDate;
    private Long listId;
}
