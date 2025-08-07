package com.railse.workforcemgmt.dto;

import lombok.Data;
import com.railse.workforcemgmt.model.Task.Priority;

@Data
public class ChangePriorityRequest {
    private Priority priority;
}

