package dev.gustavo.ToDoListAPI.utils.requests.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private byte[] icon;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime doneAt;
    private UUID taskBundleId;
    private UUID owner;
}