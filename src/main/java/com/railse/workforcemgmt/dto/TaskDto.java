package com.railse.workforcemgmt.dto;

import lombok.Data;
import com.railse.workforcemgmt.model.Task.Status;
import com.railse.workforcemgmt.model.Task.Priority;
import com.railse.workforcemgmt.model.ActivityEvent;
import com.railse.workforcemgmt.model.Comment;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDto {
    private String id;
    private String title;
    private String staffId;
    private Status status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Priority priority;
    private List<ActivityEvent> history;
    private List<Comment> comments;
}

