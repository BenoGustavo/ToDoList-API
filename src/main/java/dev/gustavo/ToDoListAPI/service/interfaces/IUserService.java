package dev.gustavo.ToDoListAPI.service.interfaces;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.requests.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for user service operations.
 * 
 * At creation should:
 * 
 * Should check if the user already exists before creating it
 * 
 * Should check if the first and last name is at least 4 characters
 * long
 * 
 * Should check if the email is valid
 * 
 * Should check if the password is at least 5 characters long
 */
public interface IUserService {

        /**
         * Retrieves a user by their unique identifier.
         *
         * @param id the unique identifier of the user
         * @return the user data transfer object
         */
        UserDTO getUserById(UUID id, HttpServletRequest request);

        /**
         * Retrieves all users.
         *
         * @return a list of user data transfer objects
         */
        List<UserDTO> getAllUsers();

        /**
         * Retrieves a user by their email.
         *
         * @param email the email of the user
         * @return the user data transfer object
         */
        UserDTO getUserByEmail(String email, HttpServletRequest request);

        /**
         * Creates a new user.
         *
         * @param user the user data transfer object (DTO)
         * 
         * @return the created user data transfer object
         */
        UserDTO create(UserModel user) throws IOException;

        /**
         * Updates an existing user.
         *
         * @param user the user data transfer object
         * @return the updated user data transfer object
         */
        UserDTO update(UUID id, UserDTO user, HttpServletRequest request);

        /**
         * Deletes a user by their unique identifier.
         *
         * @param id the unique identifier of the user
         * @return the deleted user data transfer object
         */
        UserDTO delete(UUID id, HttpServletRequest request);

        /**
         * Updates the user profile picture.
         *
         * @param newPicture the picture in a blob format
         * @param id         the unique identifier of the user
         * @return the updated user data transfer object
         */
        public UserDTO updateProfilePicture(UUID id, MultipartFile newPicture, HttpServletRequest request)
                        throws NotFound404Exception, BadRequest400Exception, SQLException, IOException;

        /**
         * Updates the user profile picture.
         *
         * @param newPassword The new password
         * @param id          the unique identifier of the user
         * @return the updated user data transfer object
         */
        UserDTO updatePassword(String email, String newPassword, String oldPassword, HttpServletRequest request)
                        throws NotFound404Exception;
}