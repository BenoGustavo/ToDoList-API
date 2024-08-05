package dev.gustavo.ToDoListAPI.utils.responses.generic;

import lombok.Data;

@Data
public class Response<T> {
    private int status;
    private String result;
    private ResponseError error;
    private T data;
}
