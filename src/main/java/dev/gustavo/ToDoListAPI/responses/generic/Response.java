package dev.gustavo.ToDoListAPI.responses.generic;

import lombok.Data;

@Data
public class Response<T> {
    private String status;
    private String result;
    private ResponseError error;
    private T data;
}
