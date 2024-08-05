package dev.gustavo.ToDoListAPI.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.service.UserService;
import dev.gustavo.ToDoListAPI.utils.requests.dto.UserDTO;
import dev.gustavo.ToDoListAPI.utils.requests.dto.converter.UserDtoConverter;
import dev.gustavo.ToDoListAPI.utils.responses.builder.ResponseBuilder;
import dev.gustavo.ToDoListAPI.utils.responses.generic.Response;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoConverter userDtoConverter;

    @PostMapping("/")
    public ResponseEntity<Response<UserDTO>> create(@RequestBody UserDTO userDTO) {
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
}
