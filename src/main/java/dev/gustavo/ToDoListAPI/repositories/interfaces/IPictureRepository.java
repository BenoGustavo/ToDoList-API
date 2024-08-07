package dev.gustavo.ToDoListAPI.repositories.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import dev.gustavo.ToDoListAPI.models.PictureModel;
import dev.gustavo.ToDoListAPI.models.UserModel;

public interface IPictureRepository extends JpaRepository<PictureModel, UUID> {

    @Transactional
    public List<PictureModel> findByOwner(UserModel owner);
}
