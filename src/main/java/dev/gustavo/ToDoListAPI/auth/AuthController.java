package dev.gustavo.ToDoListAPI.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;
import dev.gustavo.ToDoListAPI.utils.JWT.JwtUtil;
import dev.gustavo.ToDoListAPI.utils.requests.AuthRequest;
import dev.gustavo.ToDoListAPI.utils.responses.AuthResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) {
        UserDetails userDetails = authService.auth(authenticationRequest);
        final String jwt = jwtUtil.generateToken(userDetails);

        userRepository.updateLastLogin(authenticationRequest.getEmail());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}