package dev.gustavo.ToDoListAPI.repositories.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import dev.gustavo.ToDoListAPI.models.TaskModel;
import jakarta.transaction.Transactional;

@Repository
public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByTaskBundleId(UUID taskBundleModelId);

    TaskBundleModel findTaskBundleById(UUID taskId);

    List<TaskModel> findByOwnerId(UUID ownerId);

    @Transactional
    @Modifying
    @Query("UPDATE tasks u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateDeletedAt(@Param("id") UUID id);
}
