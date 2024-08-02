package dev.gustavo.ToDoListAPI.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import dev.gustavo.ToDoListAPI.models.TaskModel;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    TaskModel findByTaskBundleId(UUID taskBundleModelId);

    TaskBundleModel findTaskBundleById(UUID taskId);
}
