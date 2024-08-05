package dev.gustavo.ToDoListAPI.utils.error.custom;

public class NotFound404Exception extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final int ERROR_CODE = 404;

    public NotFound404Exception(String message) {
        super(message);
    }

    public int getCode() {
        return ERROR_CODE;
    }

}
