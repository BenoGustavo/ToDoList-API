package dev.gustavo.ToDoListAPI.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.service.UserService;
import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.NotFound404Exception;
import dev.gustavo.ToDoListAPI.utils.requests.dto.UserDTO;
import dev.gustavo.ToDoListAPI.utils.requests.dto.converter.UserDtoConverter;
import dev.gustavo.ToDoListAPI.utils.responses.builder.ResponseBuilder;
import dev.gustavo.ToDoListAPI.utils.responses.generic.Response;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoConverter userDtoConverter;

    @PostMapping("/")
    public ResponseEntity<Response<UserDTO>> create(@RequestBody UserDTO userDTO) throws IOException {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new BadRequest400Exception("Passwords don't match");
        }

        ResponseBuilder<UserDTO> responseBuilder = new ResponseBuilder<>();

        UserModel user = userDtoConverter.convertToEntity(userDTO);
        UserDTO createdUser = userService.create(user);
        createdUser.setCreatedAt(LocalDateTime.now());

        responseBuilder.data(createdUser);
        responseBuilder.status(201);
        responseBuilder.result("User created successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @GetMapping("/admin/all")
    public ResponseEntity<Response<List<UserDTO>>> getAll() {
        ResponseBuilder<List<UserDTO>> responseBuilder = new ResponseBuilder<>();

        responseBuilder.data(userService.getAllUsers()).status(200)
                .result("Users retrieved from database successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Response<UserDTO>> getById(@PathVariable("id") UUID id, HttpServletRequest request) {
        ResponseBuilder<UserDTO> responseBuilder = new ResponseBuilder<>();

        responseBuilder.data(userService.getUserById(id, request)).status(200)
                .result("User retrieved from database successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @GetMapping("/email")
    public ResponseEntity<Response<UserDTO>> getByEmail(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        ResponseBuilder<UserDTO> responseBuilder = new ResponseBuilder<>();

        responseBuilder.data(userService.getUserByEmail(userDTO.getEmail(), request)).status(200)
                .result("User retrieved from database successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @PatchMapping("/update/password")
    public ResponseEntity<Response<UserDTO>> updatePassword(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        ResponseBuilder<UserDTO> responseBuilder = new ResponseBuilder<>();

        responseBuilder
                .data(userService.updatePassword(userDTO.getEmail(), userDTO.getNewPassword(),
                        userDTO.getOldPassword(), request))
                .status(200).result("Password updated successfully");

        return ResponseEntity.status(200).body(responseBuilder.build());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response<UserDTO>> update(@PathVariable UUID id, @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        ResponseBuilder<UserDTO> responseBuilder = new ResponseBuilder<>();

        responseBuilder.data(userService.update(id, userDTO, request)).status(200).result("User updated successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @PatchMapping("/update/profile-picture/{id}")
    public ResponseEntity<Response<UserDTO>> updateProfilePicture(@PathVariable("id") UUID id,
            @RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws NotFound404Exception, BadRequest400Exception, SQLException, IOException {
        ResponseBuilder<UserDTO> responseBuilder = new ResponseBuilder<>();

        responseBuilder.data(userService.updateProfilePicture(id, file, request)).status(200)
                .result("Profile picture updated successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<UserDTO>> delete(@PathVariable("id") UUID id, HttpServletRequest request) {
        ResponseBuilder<UserDTO> responseBuilder = new ResponseBuilder<>();

        responseBuilder.data(userService.delete(id, request)).status(200).result("User deleted successfully");

        return ResponseEntity.ok(responseBuilder.build());
    }
}
