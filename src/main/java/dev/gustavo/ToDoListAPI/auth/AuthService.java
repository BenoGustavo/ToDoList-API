package dev.gustavo.ToDoListAPI.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import dev.gustavo.ToDoListAPI.service.CustomUserDetailsService;
import dev.gustavo.ToDoListAPI.utils.error.custom.BadRequest400Exception;
import dev.gustavo.ToDoListAPI.utils.error.custom.Unauthorized401Exception;
import dev.gustavo.ToDoListAPI.utils.requests.AuthRequest;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public UserDetails auth(AuthRequest authenticationRequest) {
        validateAuthRequest(authenticationRequest);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new Unauthorized401Exception("Invalid email or password");
        }

        return userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
    }

    public void validateAuthRequest(AuthRequest authenticationRequest) {
        if (authenticationRequest.getEmail() == null || authenticationRequest.getPassword() == null) {
            throw new BadRequest400Exception("Invalid auth parameters");
        }
        if (authenticationRequest.getEmail().isEmpty() || authenticationRequest.getPassword().isEmpty()) {
            throw new BadRequest400Exception("Fill the auth parameters");
        }
    }
}
