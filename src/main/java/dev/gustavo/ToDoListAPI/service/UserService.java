package dev.gustavo.ToDoListAPI.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;
import dev.gustavo.ToDoListAPI.service.interfaces.IUserService;
import dev.gustavo.ToDoListAPI.utils.EmailValidator;
import dev.gustavo.ToDoListAPI.utils.JWT.JwtUtil;
import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.Unauthorized401Exception;
import dev.gustavo.ToDoListAPI.utils.requests.dto.UserDTO;
import dev.gustavo.ToDoListAPI.utils.requests.dto.converter.UserDtoConverter;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements IUserService {

    @Autowired
    UserDtoConverter userDtoConverter;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    // Literally get the user by id
    @Override
    public UserDTO getUserById(UUID id, HttpServletRequest request) {
        isUserAuthorized(request);

        UserDTO userDto = userDtoConverter.convertToDTO(
                userRepository.findById(id).orElseThrow(() -> new NotFound404Exception("User not found")));

        return userDto;
    }

    // Literally get the user by email
    @Override
    public UserDTO getUserByEmail(String email, HttpServletRequest request) {
        isUserAuthorized(request);

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String userEmail = jwtUtil.extractEmail(token);

        if (!userEmail.equals(email)) {
            throw new BadRequest400Exception("Don't have permission to access this user");
        }

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
    public UserDTO create(UserModel user) throws IOException {
        // Validate user model
        isUserModelValid(user);

        if (!EmailValidator.isValid(user.getEmail())) {
            throw new BadRequest400Exception("Invalid email");
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(user.getHashedPassword(), BCrypt.gensalt());
        user.setHashedPassword(hashedPassword);

        // Set default picture
        user.setProfilePicture(getDefaultPicture());

        // Save on database
        userRepository.save(user);

        // Convert Model to DTO
        UserDTO userDto = userDtoConverter.convertToDTO(user);

        return userDto;
    }

    @Override
    public UserDTO delete(UUID id, HttpServletRequest request) {
        isUserAuthorized(request);

        UserModel user = userRepository.findById(id).orElseThrow(() -> new NotFound404Exception("User not found"));

        if (user.getDeletedAt() != null) {
            throw new NotFound404Exception("User already deleted!");
        }

        UUID userUUID = user.getId();

        if (!userUUID.equals(id)) {
            throw new Unauthorized401Exception("Only the owner user can delete their account");
        }

        userRepository.updateDeletedAt(id);
        UserDTO userDto = userDtoConverter.convertToDTO(userRepository.findById(id).get());

        return userDto;
    }

    @Override
    public UserDTO update(UUID id, UserDTO user, HttpServletRequest request)
            throws NotFound404Exception, BadRequest400Exception {
        isUserAuthorized(request);

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String userEmail = jwtUtil.extractEmail(token);
        UserModel userThatRequested = userRepository.findByEmail(userEmail);

        System.out.println(userThatRequested.getId() + " " + id + " " + userThatRequested.getId().equals(id) + " "
                + userEmail);

        if (!userThatRequested.getId().equals(id)) {
            throw new Unauthorized401Exception("Don't have permission to access this user");
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
    public UserDTO updateProfilePicture(UUID id, MultipartFile newPicture, HttpServletRequest request)
            throws NotFound404Exception, BadRequest400Exception, SQLException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null ? authHeader.substring(7) : null;

        if (token == null || token.isEmpty()) {
            throw new Unauthorized401Exception("Authorization token is missing");
        }

        String userEmail = jwtUtil.extractEmail(token);
        UserModel userThatRequested = userRepository.findByEmail(userEmail);

        if (!userThatRequested.getId().equals(id)) {
            throw new Unauthorized401Exception("Don't have permission to access this user");
        }

        if (newPicture == null) {
            throw new BadRequest400Exception("Invalid picture");
        }

        // Get the user from database or throw
        UserModel userModel = userRepository.findById(id)
                .orElseThrow(() -> new NotFound404Exception("User not found"));

        byte[] picture = newPicture.getBytes();

        userModel.setProfilePicture(picture);
        userModel.setUpdatedAt(LocalDateTime.now());

        // Save changes on database
        userRepository.save(userModel);

        // Trasnfrom entity in to a DTO
        UserDTO updatedUserDto = userDtoConverter.convertToDTO(userModel);

        return updatedUserDto;
    }

    @Override
    public UserDTO updatePassword(String email, String newPassword, String oldPassword, HttpServletRequest request)
            throws NotFound404Exception {

        isUserAuthorized(request);

        String currentPassword = userRepository.findByEmail(email).getHashedPassword();

        if (!BCrypt.checkpw(oldPassword, currentPassword)) {
            throw new BadRequest400Exception("Passwords don't match");
        }

        if (!isPasswordValid(newPassword)) {
            throw new BadRequest400Exception("Invalid password");
        }

        // Get the user from database or throw
        UserModel userModel = userRepository.findByEmail(email);

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        userModel.setHashedPassword(hashedPassword);
        userModel.setUpdatedAt(LocalDateTime.now());

        // Save changes on database
        userRepository.save(userModel);

        // transform entity in to a DTO
        UserDTO updatedUserDto = userDtoConverter.convertToDTO(userModel);

        return updatedUserDto;
    }

    private boolean isPasswordValid(String newPassword) {
        return (newPassword != null) && (!newPassword.isEmpty()) && (newPassword.length() >= 5);
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

        if (user.getAge() < 10 || user.getAge() > 120) {
            throw new BadRequest400Exception("Invalid age");
        }

        if (!isPasswordValid(user.getHashedPassword())) {
            System.out.println(user.getHashedPassword());
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

        if (userDTO.getAge() != userModel.getAge() && userDTO.getAge() > 9 && userDTO.getAge() < 121) {
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

    private void isUserAuthorized(HttpServletRequest request) throws Unauthorized401Exception {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        if (token == null || token.isEmpty()) {
            throw new Unauthorized401Exception("Authorization token is missing");
        }
    }

    public byte[] getDefaultPicture() throws IOException {
        File imageFile = new File("src/main/resources/static/icon/image.png");
        // Read the image
        BufferedImage image = ImageIO.read(imageFile);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

}
