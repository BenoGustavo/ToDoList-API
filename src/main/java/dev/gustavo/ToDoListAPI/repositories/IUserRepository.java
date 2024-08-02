package dev.gustavo.ToDoListAPI.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.gustavo.ToDoListAPI.models.UserModel;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);

    UserModel findByEmail(String email);
}
