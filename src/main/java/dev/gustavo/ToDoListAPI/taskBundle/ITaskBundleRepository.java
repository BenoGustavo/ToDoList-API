package dev.gustavo.ToDoListAPI.taskBundle;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItaskBundleRepository extends JpaRepository<TaskBundleModel, UUID> {

    TaskBundleModel findByTitle(String title);
}
