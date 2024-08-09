package dev.gustavo.ToDoListAPI.utils.requests.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String nickname;

    private String password;
    private String oldPassword;
    private String confirmPassword;
    private String newPassword;

    private byte[] profilePicture;
    private String email;
    private int age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime lastLogin;
}