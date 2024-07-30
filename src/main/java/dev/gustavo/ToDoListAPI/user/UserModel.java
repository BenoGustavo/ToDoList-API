package dev.gustavo.ToDoListAPI.user;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "users")
public class UserModel {
    @Id
    @Column(unique = true)
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private Blob profilePicture;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String hashedPassword;
    @Column(nullable = false)
    private int age;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Value("${null}")
    private LocalDateTime deletedAt;
    @Value("${null}")
    private LocalDateTime lastLogin;
}
