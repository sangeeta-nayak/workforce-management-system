package com.railse.workforcemgmt.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Task {
    public enum Status { ACTIVE, COMPLETED, CANCELLED }
    public enum Priority { HIGH, MEDIUM, LOW }

    private String id;
    private String title;
    private String staffId;
    private Status status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Priority priority = Priority.MEDIUM;
    private List<ActivityEvent> history = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
}

