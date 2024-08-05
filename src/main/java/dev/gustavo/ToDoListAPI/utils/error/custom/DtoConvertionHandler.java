package dev.gustavo.ToDoListAPI.utils.error.custom;

public class DtoConvertionHandler extends RuntimeException {
    private static final int ERROR_CODE = 400;

    public DtoConvertionHandler(String message) {
        super(message);
    }

    public int getCode() {
        return ERROR_CODE;
    }
}
