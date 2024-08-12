package dev.gustavo.ToDoListAPI.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.gustavo.ToDoListAPI.service.TaskBundleService;
import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskBundleDTO;
import dev.gustavo.ToDoListAPI.utils.responses.builder.ResponseBuilder;
import dev.gustavo.ToDoListAPI.utils.responses.generic.Response;

@RestController
@RequestMapping("/taskbundles")
public class TaskBundleController {
    @Autowired
    private TaskBundleService taskBundleService;

    // admin routes
    @GetMapping("/admin/all")
    public ResponseEntity<Response<List<TaskBundleDTO>>> getAll() {
        ResponseBuilder<List<TaskBundleDTO>> responseBuilder = new ResponseBuilder<>();

        List<TaskBundleDTO> taskBundle = taskBundleService.getAllTaskBundles();
        responseBuilder.data(taskBundle);
        responseBuilder.status(200);
        responseBuilder.result("TaskBundle retrieved successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    // Create new
    @PostMapping("/")
    public ResponseEntity<Response<TaskBundleDTO>> create(@RequestBody TaskBundleDTO taskBundleDTO) {
        ResponseBuilder<TaskBundleDTO> responseBuilder = new ResponseBuilder<>();

        TaskBundleDTO createdTaskBundle = taskBundleService.create(taskBundleDTO);
        responseBuilder.data(createdTaskBundle);
        responseBuilder.status(201);
        responseBuilder.result("TaskBundle created successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    // Getters
    @GetMapping("/allbyuserid/{id}")
    public ResponseEntity<Response<List<TaskBundleDTO>>> getByUserId(@PathVariable("id") UUID userId) {
        ResponseBuilder<List<TaskBundleDTO>> responseBuilder = new ResponseBuilder<>();

        List<TaskBundleDTO> taskBundle = taskBundleService.getTaskBundlesByUserId(userId);
        responseBuilder.data(taskBundle);
        responseBuilder.status(200);
        responseBuilder.result("TaskBundle retrieved successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    // Update
    @PatchMapping("/update/{id}")
    public ResponseEntity<Response<TaskBundleDTO>> update(@PathVariable("id") UUID id,
            @RequestBody TaskBundleDTO taskBundleDTO) {
        ResponseBuilder<TaskBundleDTO> responseBuilder = new ResponseBuilder<>();

        TaskBundleDTO updatedTaskBundle = taskBundleService.update(id, taskBundleDTO);
        responseBuilder.data(updatedTaskBundle);
        responseBuilder.status(200);
        responseBuilder.result("TaskBundle updated successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @PatchMapping(path = "/update/icon/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Response<TaskBundleDTO>> updateIcon(@PathVariable("id") UUID id,
            @RequestParam("file") MultipartFile newIcon) throws IOException {
        ResponseBuilder<TaskBundleDTO> responseBuilder = new ResponseBuilder<>();

        TaskBundleDTO updatedTaskBundle = taskBundleService.updateIcon(id, newIcon);
        responseBuilder.data(updatedTaskBundle);
        responseBuilder.status(200);
        responseBuilder.result("TaskBundle updated successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    // Delete (soft)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<TaskBundleDTO>> delete(@PathVariable("id") UUID id) {
        ResponseBuilder<TaskBundleDTO> responseBuilder = new ResponseBuilder<>();

        TaskBundleDTO deletedTaskBundle = taskBundleService.delete(id);
        responseBuilder.data(deletedTaskBundle);
        responseBuilder.status(200);
        responseBuilder.result("TaskBundle deleted successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }
}
