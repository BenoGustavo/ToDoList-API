package dev.gustavo.ToDoListAPI.service.interfaces;

import java.util.List;
import java.util.UUID;

import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskDTO;

/**
 * Interface for user service operations.
 * 
 * At creation should:
 * 
 * Should check if the Task already exists before creating it
 * 
 * Should check if the title name is at least 3 characters
 * long
 * 
 * Getting and updating tasks:
 * 
 * Should check if the Task exists before getting or updating it
 * 
 * Should check if the request is from the task owner
 * 
 */
public interface ITaskService {

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id the unique identifier of the task
     * @return the task data transfer object
     */
    TaskDTO getTaskById(UUID id);

    /**
     * Retrieves all tasks.
     *
     * @return a list of task data transfer objects
     */
    List<TaskDTO> getAllTasks();

    /**
     * Retrieves all tasks by their task bundle unique identifier.
     *
     * @param taskBundleId the unique identifier of the task bundle
     * @return a list of task data transfer objects
     */
    List<TaskDTO> getAllTasksByTaskBundleId(UUID taskBundleId);

    /**
     * Retrieves all tasks by their user unique identifier.
     *
     * @param userId the unique identifier of the user
     * @return a list of task data transfer objects
     */
    List<TaskDTO> getAllTasksByUserId(UUID userId);

    /**
     * Creates a new task.
     *
     * @param task the task data transfer object (DTO)
     * 
     * @return the created task data transfer object
     */
    TaskDTO create(TaskDTO task, UUID taskBundleId);

    /**
     * Updates an existing task.
     *
     * @param task the task data transfer object
     * @return the updated task data transfer object
     */
    TaskDTO update(TaskDTO task, UUID taskBundleId);

    /**
     * Deletes a task by its unique identifier.
     *
     * @param id the unique identifier of the task
     * @return the deleted task data transfer object
     */
    TaskDTO delete(UUID id);
}
