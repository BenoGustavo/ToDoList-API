package dev.gustavo.ToDoListAPI.service;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;
import dev.gustavo.ToDoListAPI.service.interfaces.IUserService;
import dev.gustavo.ToDoListAPI.utils.EmailValidator;
import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.requests.dto.UserDTO;
import dev.gustavo.ToDoListAPI.utils.requests.dto.converter.UserDtoConverter;

public class UserService implements IUserService {

    @Autowired
    UserDtoConverter userDtoConverter;

    @Autowired
    IUserRepository userRepository;

    // Literally get the user by id
    @Override
    public UserDTO getUserById(UUID id) {
        UserDTO userDto = userDtoConverter.convertToDTO(userRepository.findById(id).get());

        return userDto;
    }

    // Literally get the user by email
    @Override
    public UserDTO getUserByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        UserDTO userDto = userDtoConverter.convertToDTO(user);

        return userDto;
    }

    // Get all users, should be acessed only by admins
    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDtos = userDtoConverter.convertToDTOList(userRepository.findAll());
        return userDtos;
    }

    @Override
    public UserDTO create(UserModel user) {
        // Validate user model
        isUserModelValid(user);

        if (!EmailValidator.isValid(user.getEmail())) {
            throw new BadRequest400Exception("Invalid email");
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(user.getHashedPassword(), BCrypt.gensalt());
        user.setHashedPassword(hashedPassword);

        // Save on database
        userRepository.save(user);

        // Convert Model to DTO
        UserDTO userDto = userDtoConverter.convertToDTO(user);

        return userDto;
    }

    @Override
    public UserDTO delete(UUID id) {
        userRepository.updateDeletedAt(id);

        UserDTO userDto = userDtoConverter.convertToDTO(userRepository.findById(id).get());

        return userDto;
    }

    @Override
    public UserDTO update(UUID id, UserDTO user) throws NotFound404Exception, BadRequest400Exception {
        isUserModelValid(user);

        if (!EmailValidator.isValid(user.getEmail())) {
            throw new BadRequest400Exception("Invalid email");
        }

        // Get the user from database or throw
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new NotFound404Exception("User not found"));

        UserModel modifiedUserModel = updateModifiedValues(userModel, user);

        // Save changes on database
        userRepository.save(modifiedUserModel);

        // Trasnfrom entity in to a DTO
        UserDTO updatedUserDto = userDtoConverter.convertToDTO(modifiedUserModel);

        return updatedUserDto;
    }

    @Override
    public UserDTO updateProfilePicture(UUID id, Blob newPicture) throws NotFound404Exception, BadRequest400Exception {

        if (newPicture == null) {
            throw new BadRequest400Exception("Invalid picture");
        }

        // Get the user from database or throw
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new NotFound404Exception("User not found"));

        userModel.setProfilePicture(newPicture);
        userModel.setUpdatedAt(LocalDateTime.now());

        // Save changes on database
        userRepository.save(userModel);

        // Trasnfrom entity in to a DTO
        UserDTO updatedUserDto = userDtoConverter.convertToDTO(userModel);

        return updatedUserDto;
    }

    @Override
    public UserDTO updatePassword(UUID id, String newPassword) throws NotFound404Exception {

        if (!isPasswordValid(newPassword)) {
            throw new BadRequest400Exception("Invalid password");
        }

        // Get the user from database or throw
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new NotFound404Exception("User not found"));

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        userModel.setHashedPassword(hashedPassword);
        userModel.setUpdatedAt(LocalDateTime.now());

        // Save changes on database
        userRepository.save(userModel);

        // Trasnfrom entity in to a DTO
        UserDTO updatedUserDto = userDtoConverter.convertToDTO(userModel);

        return updatedUserDto;
    }

    private boolean isPasswordValid(String newPassword) {
        return newPassword == null || newPassword.isEmpty() || newPassword.length() < 5;
    }

    // This method wiil check if the user model is in pair with the business rules
    private boolean isUserModelValid(UserDTO user) throws BadRequest400Exception {

        if (!EmailValidator.isValid(user.getEmail())) {
            throw new BadRequest400Exception("Invalid email");
        }

        if (user.getFirstName().length() < 3 || user.getFirstName().length() > 50) {
            throw new BadRequest400Exception("Invalid first name length");
        }

        if (user.getLastName().length() < 3 || user.getLastName().length() > 50) {
            throw new BadRequest400Exception("Invalid last name length");
        }

        if (user.getNickname().length() < 3 || user.getNickname().length() > 50) {
            throw new BadRequest400Exception("Invalid nickname length");
        }

        if (user.getEmail().length() < 3 || user.getEmail().length() > 50) {
            throw new BadRequest400Exception("Invalid email length");
        }

        if (user.getAge() < 0 || user.getAge() > 120) {
            throw new BadRequest400Exception("Invalid age");
        }

        return true;
    }

    // This method wiil check if the user model is in pair with the business rules
    private boolean isUserModelValid(UserModel user) throws BadRequest400Exception {

        if (!EmailValidator.isValid(user.getEmail())) {
            throw new BadRequest400Exception("Invalid email");
        }

        if (user.getFirstName().length() < 3 || user.getFirstName().length() > 50) {
            throw new BadRequest400Exception("Invalid first name length");
        }

        if (user.getLastName().length() < 3 || user.getLastName().length() > 50) {
            throw new BadRequest400Exception("Invalid last name length");
        }

        if (user.getNickname().length() < 3 || user.getNickname().length() > 50) {
            throw new BadRequest400Exception("Invalid nickname length");
        }

        if (user.getEmail().length() < 3 || user.getEmail().length() > 50) {
            throw new BadRequest400Exception("Invalid email length");
        }

        if (user.getAge() < 0 || user.getAge() > 120) {
            throw new BadRequest400Exception("Invalid age");
        }

        if (!isPasswordValid(user.getHashedPassword())) {
            throw new BadRequest400Exception("Invalid password");
        }

        return true;
    }

    private UserModel updateModifiedValues(UserModel userModel, UserDTO userDTO) {

        boolean isUpdated = false;

        // Update the modified fields
        if (isFieldUpdated(userDTO.getFirstName(), userModel.getFirstName())) {
            userModel.setFirstName(userDTO.getFirstName());
            isUpdated = true;
        }

        if (isFieldUpdated(userDTO.getLastName(), userModel.getLastName())) {
            userModel.setLastName(userDTO.getLastName());
            isUpdated = true;
        }

        if (isFieldUpdated(userDTO.getNickname(), userModel.getNickname())) {
            userModel.setNickname(userDTO.getNickname());
            isUpdated = true;
        }

        if (isFieldUpdated(userDTO.getEmail(), userModel.getEmail())) {
            userModel.setEmail(userDTO.getEmail());
            isUpdated = true;
        }

        if (userDTO.getAge() != userModel.getAge()) {
            userModel.setAge(userDTO.getAge());
            isUpdated = true;
        }

        if (isUpdated) {
            userModel.setUpdatedAt(LocalDateTime.now());
        }

        return userModel;
    }

    private boolean isFieldUpdated(Object newValue, Object existingValue) {
        return newValue != null && !newValue.equals(existingValue);
    }

}
