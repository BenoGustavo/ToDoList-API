package dev.gustavo.ToDoListAPI.utils.responses.generic;

import lombok.Data;

@Data
public class ResponseError {
    private String code;
    private String message;
    private String description;

    public ResponseError(String code, String message) {
        this.code = code;
        this.message = message;
        this.description = message;
    }

    public ResponseError(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
