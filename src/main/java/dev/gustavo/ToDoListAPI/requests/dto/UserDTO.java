package dev.gustavo.ToDoListAPI.requests.dto;

import java.sql.Blob;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String nickname;
    private Blob profilePicture;
    private String email;
    private int age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime lastLogin;
}