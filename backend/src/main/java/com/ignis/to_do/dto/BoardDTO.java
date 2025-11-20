package com.ignis.to_do.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private boolean favorite;
    private Long ownerId;
}
