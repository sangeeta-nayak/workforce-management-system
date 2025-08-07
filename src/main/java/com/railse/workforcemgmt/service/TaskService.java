package com.railse.workforcemgmt.service;

import com.railse.workforcemgmt.model.Task;
import com.railse.workforcemgmt.model.ActivityEvent;
import com.railse.workforcemgmt.model.Comment;
import com.railse.workforcemgmt.dto.*;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class TaskService {
    private final Map<String, Task> tasks = new HashMap<>();

    public Task createTask(CreateTaskRequest req) {
        String id = UUID.randomUUID().toString();
        Task t = new Task();
        t.setId(id);
        t.setTitle(req.getTitle());
        t.setStaffId(req.getStaffId());
        t.setStartDate(req.getStartDate());
        t.setDueDate(req.getDueDate());
        t.setStatus(Task.Status.ACTIVE);
        t.getHistory().add(new ActivityEvent(Instant.now(), "Created task"));
        tasks.put(id, t);
        return t;
    }

    public List<Task> getTasksForStaffInRange(String staffId,
                                              LocalDate from,
                                              LocalDate to) {
        return tasks.values().stream()
                .filter(t -> t.getStaffId().equals(staffId))
                .filter(t -> t.getStatus() != Task.Status.CANCELLED)        // Bug 2 fix
                .filter(t -> {
                    boolean startsIn = !t.getStartDate().isBefore(from) &&
                            !t.getStartDate().isAfter(to);
                    boolean openFromBefore = t.getStartDate().isBefore(from) &&
                            t.getStatus() == Task.Status.ACTIVE;
                    return startsIn || openFromBefore;                     // Feature 1 logic
                })
                .collect(Collectors.toList());
    }

    public Optional<Task> getTask(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task reassignTask(String id, String newStaffId) {
        Task t = tasks.get(id);
        if (t == null) throw new NoSuchElementException();
        if (!t.getStaffId().equals(newStaffId)) {
            Task.Status oldStatus = t.getStatus();
            t.setStatus(Task.Status.CANCELLED);                      // Bug 1: cancel old
            t.getHistory().add(new ActivityEvent(Instant.now(),
                    "Reassigned (cancelled) from staff " + t.getStaffId()));
            // new task copy
            Task t2 = new Task();
            String newId = UUID.randomUUID().toString();
            t2.setId(newId);
            t2.setTitle(t.getTitle());
            t2.setStaffId(newStaffId);
            t2.setStatus(Task.Status.ACTIVE);
            t2.setStartDate(t.getStartDate());
            t2.setDueDate(t.getDueDate());
            t2.setPriority(t.getPriority());
            t2.getHistory().add(new ActivityEvent(Instant.now(),
                    "Created by reassignment to staff " + newStaffId));
            tasks.put(newId, t2);
            return t2;
        }
        return t;
    }

    public void changePriority(String id, ChangePriorityRequest req) {
        Task t = tasks.get(id);
        if (t == null) throw new NoSuchElementException();
        Task.Priority old = t.getPriority();
        t.setPriority(req.getPriority());
        t.getHistory().add(new ActivityEvent(Instant.now(),
                "Priority changed from " + old + " to " + req.getPriority()));
    }

    public List<Task> getTasksByPriority(Task.Priority p) {
        return tasks.values().stream()
                .filter(t -> t.getPriority() == p)
                .filter(t -> t.getStatus() != Task.Status.CANCELLED)
                .collect(Collectors.toList());
    }

    public void addComment(String id, CommentRequest req) {
        Task t = tasks.get(id);
        if (t == null) throw new NoSuchElementException();
        Comment c = new Comment(Instant.now(), req.getAuthor(), req.getText());
        t.getComments().add(c);
        t.getHistory().add(new ActivityEvent(Instant.now(),
                "Comment added by " + req.getAuthor()));
    }
}

