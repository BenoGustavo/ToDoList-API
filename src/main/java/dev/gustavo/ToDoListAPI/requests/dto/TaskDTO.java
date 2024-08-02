package dev.gustavo.ToDoListAPI.requests.dto;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private Blob icon;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime doneAt;
    private UUID taskBundleId;
}