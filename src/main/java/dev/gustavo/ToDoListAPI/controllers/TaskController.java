package dev.gustavo.ToDoListAPI.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import dev.gustavo.ToDoListAPI.models.TaskModel;
import dev.gustavo.ToDoListAPI.repositories.ITaskBundleRepository;
import dev.gustavo.ToDoListAPI.repositories.ITaskRepository;
import dev.gustavo.ToDoListAPI.utils.JWT.JwtUtil;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository TaskRepository;

    @Autowired
    private ITaskBundleRepository TaskBundleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        // Perform ownership check if needed
        return ResponseEntity.ok(this.TaskRepository.findAll());
    }

    @GetMapping("/by-task-bundle-id/{taskBundleId}")
    public ResponseEntity<Object> getByTaskBundleId(@PathVariable UUID taskBundleId,
            @RequestHeader("Authorization") String headerValue) {

        Optional<TaskBundleModel> taskBundle = this.TaskBundleRepository.findById(taskBundleId);

        if (!taskBundle.isPresent()) {
            return ResponseEntity.status(404).body("Task bundle not found");
        }

        if (this.isUserOwner(taskBundle.get(), headerValue)) {
            return ResponseEntity.status(403).body("Forbidden, you are not the owner of this task bundle");
        }

        return ResponseEntity.ok(this.TaskRepository.findByTaskBundleId(taskBundleId));
    }

    @GetMapping("/by-task-id/{taskId}")
    public ResponseEntity<Object> getById(@PathVariable UUID taskId,
            @RequestHeader("Authorization") String headerValue) {

        Optional<TaskModel> task = this.TaskRepository.findById(taskId);

        if (!task.isPresent()) {
            return ResponseEntity.status(404).body("Task not found");
        }

        if (!isUserOwner(task.get(), headerValue)) {
            return ResponseEntity.status(403).body("Forbidden, you are not the owner of this task");
        }

        return ResponseEntity.ok(this.TaskRepository.findById(taskId));
    }

    @PostMapping("/")
    public TaskModel create(TaskModel taskModel) {
        return this.TaskRepository.save(taskModel);
    }

    private boolean isUserOwner(TaskModel task, String headerValue) {
        headerValue = headerValue.replace("Bearer ", "");
        String requestEmail = jwtUtil.extractUsername(headerValue);

        TaskBundleModel taskBundle = task.getTaskBundle();

        if (taskBundle == null) {
            return false;
        }

        String ownerEmail = taskBundle.getUser().getEmail();

        return ownerEmail.equals(requestEmail);
    }

    private boolean isUserOwner(TaskBundleModel taskBundle, String headerValue) {
        headerValue = headerValue.replace("Bearer ", "");
        String requestEmail = jwtUtil.extractUsername(headerValue);

        String ownerEmail = taskBundle.getUser().getEmail();

        return ownerEmail.equals(requestEmail);
    }
}