package dev.gustavo.ToDoListAPI.utils.responses.builder;

import java.util.List;

import dev.gustavo.ToDoListAPI.utils.responses.generic.PaginationResponse;

public class PaginationResponseBuilder<T> {
    private final PaginationResponse<T> response = new PaginationResponse<>();

    public PaginationResponseBuilder<T> data(List<T> data) {
        response.setData(data);
        return this;
    }

    public PaginationResponseBuilder<T> currentPage(int currentPage) {
        response.setCurrentPage(currentPage);
        return this;
    }

    public PaginationResponseBuilder<T> totalItems(int totalItems) {
        response.setTotalItems(totalItems);
        return this;
    }

    public PaginationResponseBuilder<T> totalPages(int totalPages) {
        response.setTotalPages(totalPages);
        return this;
    }

    public PaginationResponseBuilder<T> pageSize(int pageSize) {
        response.setPageSize(pageSize);
        return this;
    }

    public PaginationResponseBuilder<T> status(int status) {
        response.setStatus(status);
        return this;
    }

    public PaginationResponseBuilder<T> result(String result) {
        response.setResult(result);
        return this;
    }

    public PaginationResponseBuilder<T> error(int code, String message) {
        response.setError(new dev.gustavo.ToDoListAPI.utils.responses.generic.ResponseError(code, message));
        return this;
    }

    // List

    public PaginationResponseBuilder<T> appendData(T data) {
        response.getData().add(data);
        return this;
    }

    public PaginationResponseBuilder<T> prependData(T data) {
        response.getData().add(0, data);
        return this;
    }

    //

    public PaginationResponse<T> build() {
        return response;
    }
}