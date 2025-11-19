package com.ignis.to_do.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListDTO {
    private Long id;
    @NotNull(message = "Name cannot be null")
    private String title;
    private Long boardId;
}
