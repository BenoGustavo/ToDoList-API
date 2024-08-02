package dev.gustavo.ToDoListAPI.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;

public interface ITaskBundleRepository extends JpaRepository<TaskBundleModel, UUID> {

    TaskBundleModel findByTitle(String title);
}
