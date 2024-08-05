package dev.gustavo.ToDoListAPI.repositories.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.gustavo.ToDoListAPI.models.permission.RoleModel;
import dev.gustavo.ToDoListAPI.utils.Enums.RoleName;

@Repository
public interface IRoleRepository extends JpaRepository<RoleModel, UUID> {
    RoleModel findByName(RoleName roleName);
}
