package dev.gustavo.ToDoListAPI.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    // @Autowired
    // private ITaskRepository TaskRepository;

    // @Autowired
    // private ITaskBundleRepository TaskBundleRepository;

    // @Autowired
    // private IUserRepository UserRepository;

    // @Autowired
    // private JwtUtil jwtUtil;

    // @GetMapping("/all")
    // public ResponseEntity<Object> getAll() {
    // // Perform ownership check if needed
    // return ResponseEntity.ok(this.TaskRepository.findAll());
    // }

    // @GetMapping("/by-task-bundle-id/{taskBundleId}")
    // public ResponseEntity<Object> getByTaskBundleId(@PathVariable UUID
    // taskBundleId,
    // @RequestHeader("Authorization") String headerValue) {

    // Optional<TaskBundleModel> taskBundle =
    // this.TaskBundleRepository.findById(taskBundleId);

    // if (!taskBundle.isPresent()) {
    // return ResponseEntity.status(404).body("Task bundle not found");
    // }

    // if (this.isUserOwner(taskBundle.get(), headerValue)) {
    // return ResponseEntity.status(403).body("Forbidden, you are not the owner of
    // this task bundle");
    // }

    // return
    // ResponseEntity.ok(this.TaskRepository.findByTaskBundleId(taskBundleId));
    // }

    // @GetMapping("/by-task-id/{taskId}")
    // public ResponseEntity<Object> getById(@PathVariable UUID taskId,
    // @RequestHeader("Authorization") String headerValue) {

    // Optional<TaskModel> task = this.TaskRepository.findById(taskId);

    // if (!task.isPresent()) {
    // return ResponseEntity.status(404).body("Task not found");
    // }

    // if (!isUserOwner(task.get(), headerValue)) {
    // return ResponseEntity.status(403).body("Forbidden, you are not the owner of
    // this task");
    // }

    // return ResponseEntity.ok(this.TaskRepository.findById(taskId));
    // }

    // @PostMapping("/")
    // public TaskModel create(TaskModel taskModel) {
    // return this.TaskRepository.save(taskModel);
    // }

    // private boolean isUserOwner(TaskModel task, String headerValue) {
    // headerValue = headerValue.replace("Bearer ", "");
    // String requestEmail = jwtUtil.extractUsername(headerValue);

    // UUID taskBundleId = task.getTaskBundleId();

    // TaskBundleModel taskBundle = TaskBundleRepository.findById(taskBundleId)
    // .orElseThrow(() -> new NotFound404Exception("task bundle not found"));

    // if (taskBundle == null) {
    // return false;
    // }

    // UUID taskBundleOwnerId = taskBundle.getUserId();

    // UserModel taskBundleOwnerModel = UserRepository.findById(taskBundleOwnerId)
    // .orElseThrow(() -> new NotFound404Exception("user not found"));

    // String ownerEmail = taskBundleOwnerModel.getEmail();

    // return ownerEmail.equals(requestEmail);
    // }

    // private boolean isUserOwner(TaskBundleModel taskBundle, String headerValue) {
    // headerValue = headerValue.replace("Bearer ", "");
    // String requestEmail = jwtUtil.extractUsername(headerValue);

    // UUID taskBundleOwnerId = taskBundle.getUserId();

    // UserModel taskBundleOwnerModel = UserRepository.findById(taskBundleOwnerId)
    // .orElseThrow(() -> new NotFound404Exception("user not found"));

    // String ownerEmail = taskBundleOwnerModel.getEmail();

    // return ownerEmail.equals(requestEmail);
    // }
}