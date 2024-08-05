package dev.gustavo.ToDoListAPI.utils.error.custom;

public class NotFound404Exception extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFound404Exception(String message) {
        super(message);
    }

}
