package dev.gustavo.ToDoListAPI.utils.requests.dto;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class TaskBundleDTO {
    private String id;
    private String title;
    private String description;
    private Blob icon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private UUID userId;
}