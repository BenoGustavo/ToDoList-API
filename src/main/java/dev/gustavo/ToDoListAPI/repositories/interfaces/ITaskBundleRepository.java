package dev.gustavo.ToDoListAPI.repositories.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.gustavo.ToDoListAPI.models.TaskBundleModel;
import jakarta.transaction.Transactional;

@Repository
public interface ITaskBundleRepository extends JpaRepository<TaskBundleModel, UUID> {

    List<TaskBundleModel> findByTitle(String title);

    List<TaskBundleModel> findByUserIdAndTitle(UUID userId, String title);

    List<TaskBundleModel> findByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("UPDATE task_bundles u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateDeletedAt(@Param("id") UUID id);
}
