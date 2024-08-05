package dev.gustavo.ToDoListAPI.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import dev.gustavo.ToDoListAPI.models.TaskModel;
import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.repositories.interfaces.ITaskBundleRepository;
import dev.gustavo.ToDoListAPI.repositories.interfaces.ITaskRepository;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;
import dev.gustavo.ToDoListAPI.service.interfaces.ITaskService;
import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.Unauthorized401Exception;
import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskDTO;
import dev.gustavo.ToDoListAPI.utils.requests.dto.converter.TaskDtoConverter;

public class TaskService implements ITaskService {

    @Autowired
    ITaskRepository taskRepository;

    @Autowired
    ITaskBundleRepository taskBundleRepository;

    @Autowired
    TaskDtoConverter taskDtoConverter;

    @Autowired
    IUserRepository userRepository;

    @Override
    public TaskDTO getTaskById(UUID id) throws Unauthorized401Exception, IllegalArgumentException {
        TaskModel task = taskRepository.findById(id).orElseThrow(() -> new NotFound404Exception("Task not found"));
        verifyOwnership(task);

        TaskDTO taskDto = taskDtoConverter.convertToDTO(task);

        return taskDto;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<TaskModel> task = taskRepository.findAll();
        List<TaskDTO> taskDto = taskDtoConverter.convertToDTOList(task);

        return taskDto;
    }

    @Override
    public List<TaskDTO> getAllTasksByTaskBundleId(UUID taskBundleId) {
        TaskBundleModel taskBundle = taskBundleRepository.findById(taskBundleId)
                .orElseThrow(() -> new NotFound404Exception("Task bundle not found"));

        UUID taskBundleOwnerId = taskBundle.getUser().getId();
        UUID currentUserId = getCurrentUserId();

        // Checks if the user is the owner of the task bundle
        if (!taskBundleOwnerId.equals(currentUserId)) {
            throw new Unauthorized401Exception("User is not the owner of the task bundle");
        }

        List<TaskModel> tasks = taskRepository.findByTaskBundleId(taskBundleId);
        List<TaskDTO> taskDtos = taskDtoConverter.convertToDTOList(tasks);

        return taskDtos;
    }

    @Override
    public List<TaskDTO> getAllTasksByUserId(UUID userId) {
        UUID currentUserId = getCurrentUserId();

        // Checks if the user is the owner of those task
        if (!userId.equals(currentUserId)) {
            throw new Unauthorized401Exception("user is not the owner of those task");
        }

        List<TaskModel> tasks = taskRepository.findByOwnerId(userId);
        List<TaskDTO> taskDtos = taskDtoConverter.convertToDTOList(tasks);

        return taskDtos;
    }

    @Override
    public TaskDTO create(TaskDTO task, UUID taskBundleId) {
        UUID currrentUserId = getCurrentUserId();

        // Getting the owner and the task bundle
        UserModel owner = userRepository.findById(currrentUserId)
                .orElseThrow(() -> new NotFound404Exception("User not found"));
        TaskBundleModel taskBundle = taskBundleRepository.findById(taskBundleId)
                .orElseThrow(() -> new NotFound404Exception("Task bundle not found"));

        // Converting from DTO to model
        TaskModel taskModel = taskDtoConverter.convertToModel(task, taskBundle, owner);

        // Setting the owner and the task bundle
        taskModel.setOwner(owner);
        taskModel.setTaskBundle(taskBundle);

        // Validating the task
        isTaskModelValid(taskModel);

        // Saving on the database
        taskRepository.save(taskModel);

        // Converting to DTO then returning
        TaskDTO taskDto = taskDtoConverter.convertToDTO(taskModel);
        return taskDto;
    }

    @Override
    public TaskDTO update(TaskDTO task) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public TaskDTO delete(UUID id) {
        TaskModel task = taskRepository.findById(id).orElseThrow(
                () -> new NotFound404Exception("Task not found"));

        verifyOwnership(task);

        taskRepository.updateDeletedAt(id);
        TaskDTO taskDto = taskDtoConverter.convertToDTO(task);

        return taskDto;
    }

    private boolean isTaskModelValid(TaskModel task) {
        if (task.getTitle() == null || task.getTitle().length() < 4) {
            throw new BadRequest400Exception("Invalid title");
        }

        if (task.getDescription().length() < 5) {
            throw new BadRequest400Exception("Invalid description");
        }

        return true;
    }

    private UUID getCurrentUserId() throws Unauthorized401Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserModel) {
            return ((UserModel) authentication.getPrincipal()).getId();
        }

        throw new Unauthorized401Exception("User is not authenticated");
    }

    private void verifyOwnership(TaskModel task) throws Unauthorized401Exception {
        UUID currentUserId = getCurrentUserId();

        if (!task.getOwner().getId().equals(currentUserId)) {
            throw new Unauthorized401Exception("User is not the owner of the task");
        }
    }
}
