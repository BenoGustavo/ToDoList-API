package dev.gustavo.ToDoListAPI.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private ITaskRepository TaskRepository;

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(this.TaskRepository.findAll());
    }

    @GetMapping("/by-task-bundle-id/{taskBundleId}")
    public ResponseEntity<Object> getByTaskBundleId(@PathVariable UUID taskBundleId) {
        return ResponseEntity.ok(this.TaskRepository.findByTaskBundleId(taskBundleId));
    }

    @GetMapping("/by-id")
    public ResponseEntity<Object> getById(UUID taskId) {
        return ResponseEntity.ok(this.TaskRepository.findById(taskId));
    }

    @PostMapping("/")
    public TaskModel create(TaskModel taskModel) {
        return this.TaskRepository.save(taskModel);
    }
}
