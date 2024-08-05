package dev.gustavo.ToDoListAPI.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-bundles")
public class TaskBundleController {
    // @Autowired
    // private ITaskBundleRepository taskBundleRepository;

    // @Autowired
    // private IUserRepository userRepository;

    // @GetMapping("/all")
    // public ResponseEntity<Object> getAll() {
    // return ResponseEntity.ok(this.taskBundleRepository.findAll());
    // }

    // @PostMapping("/")
    // public ResponseEntity<Object> create(@RequestBody TaskBundleModel
    // taskBundleModel) {

    // if (taskBundleModel.getUser() == null) {
    // return ResponseEntity.badRequest().body("User is required");
    // }

    // UUID userId = taskBundleModel.getUser().getId();

    // Optional<UserModel> user = this.userRepository.findById(userId);

    // if (user.get() == null) {
    // return ResponseEntity.status(404).body("User not found");
    // }

    // taskBundleModel.setUser(user.get());

    // this.taskBundleRepository.save(taskBundleModel);

    // return ResponseEntity.ok(taskBundleModel);
    // }
}
