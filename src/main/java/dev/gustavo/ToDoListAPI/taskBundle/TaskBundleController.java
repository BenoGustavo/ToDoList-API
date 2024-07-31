package dev.gustavo.ToDoListAPI.taskBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-bundles")
public class TaskBundleController {
    @Autowired
    private ItaskBundleRepository taskBundleRepository;

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(this.taskBundleRepository.findAll());
    }

    @PostMapping("/")
    public TaskBundleModel create(TaskBundleModel taskBundleModel) {
        return this.taskBundleRepository.save(taskBundleModel);
    }
}
