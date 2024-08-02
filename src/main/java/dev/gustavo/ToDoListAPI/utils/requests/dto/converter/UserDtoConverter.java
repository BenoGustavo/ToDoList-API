package dev.gustavo.ToDoListAPI.utils.requests.dto.converter;

import java.util.UUID;

import org.springframework.util.StringUtils;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.utils.error.custom.DtoConvertionHandler;
import dev.gustavo.ToDoListAPI.utils.requests.dto.UserDTO;

public class UserDtoConverter {
    // Convert UserModel to UserDTO
    public UserDTO convertToDTO(UserModel user) {
        UserDTO dto = new UserDTO();

        dto.setId(user.getId().toString());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setNickname(user.getNickname());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setDeletedAt(user.getDeletedAt());
        dto.setLastLogin(user.getLastLogin());
        return dto;
    }

    // Convert UserDTO to UserModel
    public UserModel convertToEntity(UserDTO dto) {
        validateUserDTO(dto);
        UserModel user = new UserModel();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setNickname(dto.getNickname());
        user.setProfilePicture(dto.getProfilePicture());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());

        user.setId(UUID.fromString(dto.getId()));
        return user;
    }

    private void validateUserDTO(UserDTO dto) {
        if (!StringUtils.hasText(dto.getFirstName())) {
            throw new DtoConvertionHandler("First name is required");
        }
        if (!StringUtils.hasText(dto.getLastName())) {
            throw new DtoConvertionHandler("Last name is required");
        }
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new DtoConvertionHandler("Email is required");
        }
        if (!StringUtils.hasText(dto.getId())) {
            throw new DtoConvertionHandler("Id is required");
        }
        if (!StringUtils.hasText(dto.getNickname())) {
            throw new DtoConvertionHandler("Nickname is required");
        }
    }
}