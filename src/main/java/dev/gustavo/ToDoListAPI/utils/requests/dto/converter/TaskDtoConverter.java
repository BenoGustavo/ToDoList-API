package dev.gustavo.ToDoListAPI.utils.requests.dto.converter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import dev.gustavo.ToDoListAPI.models.TaskModel;
import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.utils.error.custom.DtoConvertionHandler;
import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskDTO;

@Component
public class TaskDtoConverter {

    public TaskModel convertToModel(TaskDTO taskDTO, TaskBundleModel taskBundle, UserModel owner) {
        validateTaskDTO(taskDTO);
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskDTO.getId() != null ? UUID.fromString(taskDTO.getId()) : null);
        taskModel.setTitle(taskDTO.getTitle());
        taskModel.setDescription(taskDTO.getDescription());
        taskModel.setIcon(taskDTO.getIcon());
        taskModel.setDeadline(taskDTO.getDeadline());
        taskModel.setCreatedAt(taskDTO.getCreatedAt());
        taskModel.setUpdatedAt(taskDTO.getUpdatedAt());
        taskModel.setDeletedAt(taskDTO.getDeletedAt());
        taskModel.setDoneAt(taskDTO.getDoneAt());
        taskModel.setTaskBundle(taskBundle);
        taskModel.setOwner(owner);

        return taskModel;
    }

    public TaskDTO convertToDTO(TaskModel taskModel) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(taskModel.getId() != null ? taskModel.getId().toString() : null);
        taskDTO.setTitle(taskModel.getTitle());
        taskDTO.setDescription(taskModel.getDescription());
        taskDTO.setIcon(taskModel.getIcon());
        taskDTO.setDeadline(taskModel.getDeadline());
        taskDTO.setCreatedAt(taskModel.getCreatedAt());
        taskDTO.setUpdatedAt(taskModel.getUpdatedAt());
        taskDTO.setDeletedAt(taskModel.getDeletedAt());
        taskDTO.setDoneAt(taskModel.getDoneAt());
        taskDTO.setTaskBundleId(taskModel.getTaskBundle().getId());
        taskDTO.setOwner(taskModel.getOwner().getId());

        return taskDTO;
    }

    public List<TaskDTO> convertToDTOList(List<TaskModel> taskModels) {
        return taskModels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateTaskDTO(TaskDTO taskDTO) {
        if (taskDTO.getTitle() == null || taskDTO.getTitle().isEmpty()) {
            throw new DtoConvertionHandler("Title is required");
        }
        if (taskDTO.getTaskBundleId() == null) {
            throw new DtoConvertionHandler("TaskBundleId is required");
        }
        if (taskDTO.getOwner() == null) {
            throw new DtoConvertionHandler("Owner is required");
        }
    }
}