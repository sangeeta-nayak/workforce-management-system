package com.railse.workforcemgmt.controller;

import com.railse.workforcemgmt.dto.CommentRequest;
import com.railse.workforcemgmt.model.Comment;
import com.railse.workforcemgmt.model.Task;
import com.railse.workforcemgmt.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Comparator;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {

    private final TaskService taskService;

    public CommentController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public void addComment(@PathVariable String taskId, @RequestBody CommentRequest request) {
        taskService.addComment(taskId, request);
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable String taskId) {
        Task task = taskService.getTask(taskId).orElseThrow();
        return task.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getTimestamp))
                .toList();
    }
}

