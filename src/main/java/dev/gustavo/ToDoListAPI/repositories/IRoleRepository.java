package dev.gustavo.ToDoListAPI.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.gustavo.ToDoListAPI.models.permission.RoleModel;
import dev.gustavo.ToDoListAPI.utils.RoleName;

public interface IRoleRepository extends JpaRepository<RoleModel, UUID> {
    RoleModel findByName(RoleName roleName);
}
