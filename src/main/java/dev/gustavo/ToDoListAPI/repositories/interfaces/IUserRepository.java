package dev.gustavo.ToDoListAPI.repositories.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.gustavo.ToDoListAPI.models.UserModel;
import jakarta.transaction.Transactional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByNickname(String nickname);

    UserModel findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE users u SET u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateDeletedAt(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE users u SET u.lastLogin = CURRENT_TIMESTAMP WHERE u.email = :email")
    void updateLastLogin(@Param("email") String email);
}
