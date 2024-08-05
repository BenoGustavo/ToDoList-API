package dev.gustavo.ToDoListAPI.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Root {

    @GetMapping
    public String root() {
        return "<h1 style='width:100%; text-align:center; margin-top:48vh;'>Welcome to the ToDoListAPI</h1>";
    }
}
