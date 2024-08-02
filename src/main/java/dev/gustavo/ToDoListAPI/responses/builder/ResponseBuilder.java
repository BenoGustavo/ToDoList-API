package dev.gustavo.ToDoListAPI.responses.builder;

import dev.gustavo.ToDoListAPI.responses.generic.Response;
import dev.gustavo.ToDoListAPI.responses.generic.ResponseError;

public class ResponseBuilder<T> {
    private final Response<T> response = new Response<>();

    public ResponseBuilder<T> status(String status) {
        response.setStatus(status);
        return this;
    }

    public ResponseBuilder<T> result(String result) {
        response.setResult(result);
        return this;
    }

    public ResponseBuilder<T> error(String code, String message) {
        response.setError(new ResponseError(code, message));
        return this;
    }

    public ResponseBuilder<T> error(String code, String message, String description) {
        response.setError(new ResponseError(code, message, description));
        return this;
    }

    public ResponseBuilder<T> data(T data) {
        response.setData(data);
        return this;
    }

    public Response<T> build() {
        return response;
    }
}
