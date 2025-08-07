package com.railse.workforcemgmt.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest {
    private String title;
    private String staffId;
    private LocalDate startDate;
    private LocalDate dueDate;
}

