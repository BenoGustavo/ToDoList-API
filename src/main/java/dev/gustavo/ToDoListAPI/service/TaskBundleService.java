package dev.gustavo.ToDoListAPI.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import dev.gustavo.ToDoListAPI.models.TaskModel;
import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.repositories.interfaces.ITaskBundleRepository;
import dev.gustavo.ToDoListAPI.repositories.interfaces.ITaskRepository;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;
import dev.gustavo.ToDoListAPI.service.interfaces.ITaskBundleService;
import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.Unauthorized401Exception;
import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskBundleDTO;
import dev.gustavo.ToDoListAPI.utils.requests.dto.converter.TaskBundleDtoConverter;

@Service
public class TaskBundleService implements ITaskBundleService {
    @Autowired
    ITaskRepository taskRepository;

    @Autowired
    ITaskBundleRepository taskBundleRepository;

    @Autowired
    TaskBundleDtoConverter taskBundleDtoConverter;

    @Autowired
    IUserRepository userRepository;

    @Override
    public List<TaskBundleDTO> getTaskBundleByTitleAndUserId(UUID userId, String title) {
        UUID currentUserId = getCurrentUserId();
        boolean IsUserOwner = userId.equals(currentUserId);

        if (!IsUserOwner) {
            throw new Unauthorized401Exception("User is not the owner of those task");
        }

        List<TaskBundleModel> taskBundle = taskBundleRepository.findByUserIdAndTitle(userId, title);
        List<TaskBundleDTO> taskBundleDto = taskBundleDtoConverter.convertToDTOList(taskBundle);

        return taskBundleDto;
    }

    @Override
    public List<TaskBundleDTO> getTaskBundleByTitle(String title) {
        List<TaskBundleModel> taskBundle = taskBundleRepository.findByTitle(title);
        List<TaskBundleDTO> taskBundleDto = taskBundleDtoConverter.convertToDTOList(taskBundle);

        return taskBundleDto;
    }

    @Override
    public List<TaskBundleDTO> getAllTaskBundles() {
        List<TaskBundleModel> taskBundle = taskBundleRepository.findAll();
        List<TaskBundleDTO> taskBundleDto = taskBundleDtoConverter.convertToDTOList(taskBundle);

        return taskBundleDto;
    }

    @Override
    public List<TaskBundleDTO> getTaskBundlesByUserId(UUID userId) {
        UUID currentUserId = getCurrentUserId();
        boolean IsUserOwner = userId.equals(currentUserId);

        if (!IsUserOwner) {
            throw new Unauthorized401Exception("User is not the owner of those task bundles");
        }

        List<TaskBundleModel> taskBundle = taskBundleRepository.findByUserId(userId);
        List<TaskBundleDTO> taskBundleDto = taskBundleDtoConverter.convertToDTOList(taskBundle);

        return taskBundleDto;
    }

    @Override
    public TaskBundleDTO create(TaskBundleDTO taskBundleDto) {
        UUID currentUserId = getCurrentUserId();

        taskBundleDto.setUserId(currentUserId);
        validateTaskBundle(taskBundleDto);

        UserModel owner = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFound404Exception("User not found"));
        TaskBundleModel taskBundle = taskBundleDtoConverter.convertToModel(taskBundleDto, owner);

        taskBundleRepository.save(taskBundle);

        return taskBundleDto;
    }

    @Override
    public TaskBundleDTO update(TaskBundleDTO taskBundle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public TaskBundleDTO delete(UUID id) {
        TaskBundleModel taskBundle = taskBundleRepository.findById(id)
                .orElseThrow(() -> new NotFound404Exception("TaskBundle not found"));

        UUID currentUserId = getCurrentUserId();
        boolean IsUserOwner = taskBundle.getUser().getId().equals(currentUserId);

        if (!IsUserOwner) {
            throw new Unauthorized401Exception("User is not the owner of this task bundle");
        }

        // Soft delete bundle
        taskBundleRepository.updateDeletedAt(id);

        // Soft delete all tasks
        List<TaskModel> tasks = taskRepository.findByTaskBundleId(id);
        for (TaskModel task : tasks) {
            taskRepository.updateDeletedAt(task.getId());
        }

        TaskBundleDTO taskBundleDto = taskBundleDtoConverter.convertToDTO(taskBundle);

        return taskBundleDto;
    }

    private void validateTaskBundle(TaskBundleDTO taskBundle) {
        if (taskBundle == null) {
            throw new BadRequest400Exception("Invalid task bundle");
        }

        if (taskBundle.getId() != null) {
            throw new BadRequest400Exception("Invalid invalid id");
        }

        if (taskBundle.getCreatedAt() != null) {
            throw new BadRequest400Exception("Invalid creation date");
        }

        if (taskBundle.getTitle().length() < 3 || taskBundle.getTitle().length() > 255) {
            throw new BadRequest400Exception("Invalid title");
        }

        if (taskBundle.getDescription().length() < 3 || taskBundle.getDescription().length() > 255) {
            throw new BadRequest400Exception("Invalid description");
        }
    }

    private UUID getCurrentUserId() throws Unauthorized401Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserModel) {
            return ((UserModel) authentication.getPrincipal()).getId();
        }

        throw new Unauthorized401Exception("User is not authenticated");
    }

}
