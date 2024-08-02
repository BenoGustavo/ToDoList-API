package dev.gustavo.ToDoListAPI.service;

import java.util.List;
import java.util.UUID;

import dev.gustavo.ToDoListAPI.service.interfaces.ITaskService;
import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskDTO;

public class TaskService implements ITaskService {

    @Override
    public TaskDTO getTaskById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTaskById'");
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTasks'");
    }

    @Override
    public List<TaskDTO> getAllTasksByTaskBundleId(UUID taskBundleId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTasksByTaskBundleId'");
    }

    @Override
    public List<TaskDTO> getAllTasksByUserId(UUID userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTasksByUserId'");
    }

    @Override
    public TaskDTO create(TaskDTO task) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public TaskDTO update(TaskDTO task) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public TaskDTO delete(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
