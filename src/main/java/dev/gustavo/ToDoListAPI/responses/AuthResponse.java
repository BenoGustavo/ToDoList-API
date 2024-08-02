package dev.gustavo.ToDoListAPI.responses;

public class AuthResponse {

    private final String token;

    public AuthResponse(String jwt) {
        this.token = jwt;
    }

    public String getToken() {
        return token;
    }
}