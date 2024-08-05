package dev.gustavo.ToDoListAPI.utils.error.custom;

public class BadRequest400Exception extends RuntimeException {

    public BadRequest400Exception(String message) {
        super(message);
    }

}
