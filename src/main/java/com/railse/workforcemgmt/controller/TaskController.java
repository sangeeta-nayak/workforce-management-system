package com.railse.workforcemgmt.controller;

import com.railse.workforcemgmt.dto.*;
import com.railse.workforcemgmt.model.ActivityEvent;
import com.railse.workforcemgmt.model.Task;
import com.railse.workforcemgmt.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService svc;

    public TaskController(TaskService svc) { this.svc = svc; }

    @PostMapping
    public TaskDto create(@RequestBody CreateTaskRequest req) {
        Task t = svc.createTask(req);
        return toDto(t);
    }

    @GetMapping
    public List<TaskDto> listInRange(@RequestParam String staffId,
                                     @RequestParam String from,
                                     @RequestParam String to) {
        LocalDate f = LocalDate.parse(from);
        LocalDate t2 = LocalDate.parse(to);
        return svc.getTasksForStaffInRange(staffId, f, t2)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @PostMapping("/{id}/reassign")
    public TaskDto reassign(@PathVariable String id,
                            @RequestParam String newStaffId) {
        Task t = svc.reassignTask(id, newStaffId);
        return toDto(t);
    }

    @PostMapping("/{id}/priority")
    public void changePriority(@PathVariable String id,
                               @RequestBody ChangePriorityRequest req) {
        svc.changePriority(id, req);
    }

    @GetMapping("/priority/{priority}")
    public List<TaskDto> listByPriority(@PathVariable Task.Priority priority) {
        return svc.getTasksByPriority(priority)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @PostMapping("/{id}/comment")
    public void addComment(@PathVariable String id,
                           @RequestBody CommentRequest req) {
        svc.addComment(id, req);
    }

    @GetMapping("/{id}")
    public TaskDto getOne(@PathVariable String id) {
        return svc.getTask(id)
                .map(this::toDto)
                .orElseThrow();
    }

    private TaskDto toDto(Task t) {
        TaskDto d = new TaskDto();
        d.setId(t.getId());
        d.setTitle(t.getTitle());
        d.setStaffId(t.getStaffId());
        d.setStatus(t.getStatus());
        d.setStartDate(t.getStartDate());
        d.setDueDate(t.getDueDate());
        d.setPriority(t.getPriority());
        d.setHistory(t.getHistory().stream().sorted(
                Comparator.comparing(ActivityEvent::getTimestamp)
        ).collect(Collectors.toList()));
        d.setComments(t.getComments().stream().sorted(
                Comparator.comparing(c -> c.getTimestamp())
        ).collect(Collectors.toList()));
        return d;
    }
}

