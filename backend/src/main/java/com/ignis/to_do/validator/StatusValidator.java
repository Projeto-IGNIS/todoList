package com.ignis.to_do.validator;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusValidator {
    private String status;
    private List<String> statusList = List.of("PENDING", "IN_PROGRESS", "DONE");
    public StatusValidator(String status) {
        this.status = status;
    }

    public boolean validateStatus(String status) {
        
        return statusList.contains(status);
    }

}
