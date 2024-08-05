package dev.gustavo.ToDoListAPI.utils.error.custom;

public class BadRequest400Exception extends RuntimeException {
    private final int ERROR_CODE = 400;

    public BadRequest400Exception(String message) {
        super(message);
    }

    public int getCode() {
        return ERROR_CODE;
    }
}
