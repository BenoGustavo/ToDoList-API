package dev.gustavo.ToDoListAPI.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.gustavo.ToDoListAPI.service.TaskService;
import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskDTO;
import dev.gustavo.ToDoListAPI.utils.responses.builder.ResponseBuilder;
import dev.gustavo.ToDoListAPI.utils.responses.generic.Response;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    // Getters
    @GetMapping("/{id}")
    public ResponseEntity<Response<TaskDTO>> getTaskById(@PathVariable UUID id) {
        ResponseBuilder<TaskDTO> responseBuilder = new ResponseBuilder<>();

        TaskDTO task = taskService.getTaskById(id);
        responseBuilder.data(task);
        responseBuilder.status(200);
        responseBuilder.result("Task retrieved successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @GetMapping("/admin/all")
    public ResponseEntity<Response<List<TaskDTO>>> getAllTasks() {
        ResponseBuilder<List<TaskDTO>> responseBuilder = new ResponseBuilder<>();

        List<TaskDTO> task = taskService.getAllTasks();
        responseBuilder.data(task);
        responseBuilder.status(200);
        responseBuilder.result("Task retrieved successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @GetMapping("/allbytaskbundleid/{taskBundleId}")
    public ResponseEntity<Response<List<TaskDTO>>> getAllTasksByTaskBundleId(@PathVariable UUID taskBundleId) {
        ResponseBuilder<List<TaskDTO>> responseBuilder = new ResponseBuilder<>();

        List<TaskDTO> task = taskService.getAllTasksByTaskBundleId(taskBundleId);
        responseBuilder.data(task);
        responseBuilder.status(200);
        responseBuilder.result("Tasks retrieved successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @GetMapping("/alltaskbyuserid/{userId}")
    public ResponseEntity<Response<List<TaskDTO>>> getAllTasksByUserId(@PathVariable UUID userId) {
        ResponseBuilder<List<TaskDTO>> responseBuilder = new ResponseBuilder<>();

        List<TaskDTO> task = taskService.getAllTasksByUserId(userId);
        responseBuilder.data(task);
        responseBuilder.status(200);
        responseBuilder.result("Tasks retrieved successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @PostMapping("/")
    public ResponseEntity<Response<TaskDTO>> createTask(TaskDTO taskDTO) {
        ResponseBuilder<TaskDTO> responseBuilder = new ResponseBuilder<>();

        TaskDTO task = taskService.create(taskDTO, taskDTO.getTaskBundleId());
        responseBuilder.data(task);
        responseBuilder.status(201);
        responseBuilder.result("Task created successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response<TaskDTO>> updateTask(@PathVariable UUID id, TaskDTO taskDTO) {
        ResponseBuilder<TaskDTO> responseBuilder = new ResponseBuilder<>();

        TaskDTO task = taskService.update(taskDTO, id);
        responseBuilder.data(task);
        responseBuilder.status(200);
        responseBuilder.result("Task updated successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Response<TaskDTO>> deleteTask(@PathVariable UUID id) {
        ResponseBuilder<TaskDTO> responseBuilder = new ResponseBuilder<>();

        TaskDTO task = taskService.delete(id);
        responseBuilder.data(task);
        responseBuilder.status(200);
        responseBuilder.result("Task deleted successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }
}