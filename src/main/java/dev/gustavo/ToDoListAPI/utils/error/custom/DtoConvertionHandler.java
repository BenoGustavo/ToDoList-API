package dev.gustavo.ToDoListAPI.utils.error.custom;

public class DtoConvertionHandler extends RuntimeException {
    public DtoConvertionHandler(String message) {
        super(message);
    }
}
