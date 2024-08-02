package dev.gustavo.ToDoListAPI.error.custom;

public class DtoConvertionHandler extends RuntimeException {
    public DtoConvertionHandler(String message) {
        super(message);
    }
}
