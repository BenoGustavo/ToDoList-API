package dev.gustavo.ToDoListAPI.utils.error.custom;

public class Unauthorized401Exception extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public Unauthorized401Exception(String message) {
        super(message);
    }

}
