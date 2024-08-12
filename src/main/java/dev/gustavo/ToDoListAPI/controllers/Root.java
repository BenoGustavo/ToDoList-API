package dev.gustavo.ToDoListAPI.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Root {

    @GetMapping
    public String root() {
        return "<p>Welcome to the TODO-LIST API, see the docs in <a href='http://localhost:8080/swagger-ui/index.html'>/api-docs</p>";
    }

    @GetMapping("/api-docs")
    public String redirectToSwagger() {
        return "redirect:http://localhost:8080/swagger-ui/index.html";
    }
}
