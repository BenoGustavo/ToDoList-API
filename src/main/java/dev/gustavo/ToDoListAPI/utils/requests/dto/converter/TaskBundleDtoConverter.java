package dev.gustavo.ToDoListAPI.utils.requests.dto.converter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskBundleDTO;

@Component
public class TaskBundleDtoConverter {
    public TaskBundleDTO convertToDTO(TaskBundleModel taskBundleModel) {
        TaskBundleDTO taskBundleDTO = new TaskBundleDTO();
        taskBundleDTO.setId(taskBundleModel.getId().toString());
        taskBundleDTO.setTitle(taskBundleModel.getTitle());
        taskBundleDTO.setDescription(taskBundleModel.getDescription());
        taskBundleDTO.setIcon(taskBundleModel.getIcon());
        taskBundleDTO.setCreatedAt(taskBundleModel.getCreatedAt());
        taskBundleDTO.setUpdatedAt(taskBundleModel.getUpdatedAt());
        taskBundleDTO.setDeletedAt(taskBundleModel.getDeletedAt());
        taskBundleDTO.setUserId(taskBundleModel.getUser().getId());
        return taskBundleDTO;
    }

    public TaskBundleModel convertToModel(TaskBundleDTO taskBundleDTO, UserModel owner) {
        TaskBundleModel taskBundleModel = new TaskBundleModel();
        if (taskBundleDTO.getId() != null) {
            taskBundleModel.setId(UUID.fromString(taskBundleDTO.getId()));
        }
        taskBundleModel.setTitle(taskBundleDTO.getTitle());
        taskBundleModel.setDescription(taskBundleDTO.getDescription());
        taskBundleModel.setIcon(taskBundleDTO.getIcon());
        taskBundleModel.setCreatedAt(taskBundleDTO.getCreatedAt());
        taskBundleModel.setUpdatedAt(taskBundleDTO.getUpdatedAt());
        taskBundleModel.setDeletedAt(taskBundleDTO.getDeletedAt());
        taskBundleModel.setUser(owner);
        return taskBundleModel;
    }

    public List<TaskBundleDTO> convertToDTOList(List<TaskBundleModel> taskBundleModels) {
        return taskBundleModels.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskBundleModel> convertToModelList(List<TaskBundleDTO> taskBundleDTOs, UserModel userModel) {
        return taskBundleDTOs.stream()
                .map(taskBundleDTO -> convertToModel(taskBundleDTO, userModel))
                .collect(Collectors.toList());
    }
}
