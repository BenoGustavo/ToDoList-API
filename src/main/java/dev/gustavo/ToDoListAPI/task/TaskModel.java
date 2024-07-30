package dev.gustavo.ToDoListAPI.task;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import dev.gustavo.ToDoListAPI.taskBundle.TaskBundleModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "tasks")
public class TaskModel {
    @Id
    @Column(unique = true)
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String title;
    private String description;
    private Blob icon;
    private LocalDateTime deadline;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Value("${null}")
    private LocalDateTime deletedAt;
    @Value("${null}")
    private LocalDateTime doneAt;

    @ManyToOne
    @JoinColumn(name = "task_bundle_id", nullable = false)
    private TaskBundleModel taskBundle;
}
