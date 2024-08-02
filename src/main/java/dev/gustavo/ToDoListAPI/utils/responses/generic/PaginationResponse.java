package dev.gustavo.ToDoListAPI.utils.responses.generic;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaginationResponse<T> extends Response<List<T>> {
    private List<T> data;
    private int currentPage;
    private int totalItems;
    private int totalPages;
    private int pageSize;

    public PaginationResponse() {
    }

    public PaginationResponse(List<T> data, int currentPage, int totalItems, int totalPages, int pageSize) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }
}