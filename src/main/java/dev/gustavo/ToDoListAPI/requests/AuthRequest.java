package dev.gustavo.ToDoListAPI.requests;

public class AuthRequest {

    private String email;
    private String password;

    // Constructors
    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.email = username;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}