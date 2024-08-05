package dev.gustavo.ToDoListAPI.service.interfaces;

import java.util.List;
import java.util.UUID;

import dev.gustavo.ToDoListAPI.utils.requests.dto.TaskBundleDTO;

/**
 * Interface for task bundle service operations.
 * 
 * At creation should:
 * 
 * Should check if the TaskBundle already exists before creating it
 * 
 * Should check if the title name is at least 3 characters
 * long
 * 
 * Getting and updating task bundles:
 * 
 * Should check if the TaskBundle exists before getting or updating it
 * 
 * Should check if the request is from the task bundle owner
 * 
 * Removing task bundles:
 * 
 * Should warn the user about all tasks in the bundle being deleted
 * 
 */
public interface ITaskBundleService {

    /**
     * Retrieves a task bundle by its unique identifier.
     *
     * @param id    the unique identifier of the user
     * @param title the title of the task bundle
     * @return the task bundle data transfer object
     */
    public List<TaskBundleDTO> getTaskBundleByTitleAndUserId(UUID userId, String title);

    /**
     * Retrieves a task bundle by its unique identifier.
     *
     * @param title the title of the task bundle
     * @return the task bundle data transfer object
     */
    public List<TaskBundleDTO> getTaskBundleByTitle(String title);

    /**
     * Retrieves all task bundles.
     *
     * @return a list of task bundle data transfer objects
     */
    List<TaskBundleDTO> getAllTaskBundles();

    /**
     * Retrieves all task bundles by their user unique identifier.
     *
     * @param userId the unique identifier of the user
     * @return a list of task bundle data transfer objects
     */
    List<TaskBundleDTO> getTaskBundlesByUserId(UUID userId);

    /**
     * Creates a new task bundle.
     *
     * @param taskBundle the task bundle data transfer object (DTO)
     * 
     * @return the created task bundle data transfer object
     */
    TaskBundleDTO create(TaskBundleDTO taskBundle);

    /**
     * Updates an existing task bundle.
     *
     * @param taskBundle the task bundle data transfer object
     * @return the updated task bundle data transfer object
     */
    TaskBundleDTO update(TaskBundleDTO taskBundle);

    /**
     * Deletes a task bundle by its unique identifier.
     *
     * @param id the unique identifier of the task bundle
     * @return the deleted task bundle data transfer object
     */
    TaskBundleDTO delete(UUID id);
}
