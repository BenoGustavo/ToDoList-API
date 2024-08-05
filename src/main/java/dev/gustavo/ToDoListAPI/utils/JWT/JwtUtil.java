package dev.gustavo.ToDoListAPI.utils.JWT;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import dev.gustavo.ToDoListAPI.models.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 */
@Component
public class JwtUtil {

    private final byte[] secretKey;

    public String getCurrentUserJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserModel) {
            // UserModel user = (UserModel) authentication.getPrincipal();
            return (String) authentication.getCredentials();
        }
        return null;
    }

    /**
     * Constructor that initializes the secret key from the environment properties.
     *
     * @param env the environment properties
     * @throws RuntimeException if the JWT_SECRET_KEY is not set
     */
    @Autowired
    public JwtUtil(Environment env) {
        String secretKeyString = env.getProperty("JWT_SECRET_KEY");

        if (secretKeyString == null) {
            throw new RuntimeException("JWT_SECRET_KEY is not set");
        }

        this.secretKey = Base64.getDecoder().decode(secretKeyString);
    }

    /**
     * Generates a JWT token for the given authentication.
     *
     * @param authentication the authentication object
     * @return the generated JWT token
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details object
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();

        System.out.println("Generating token for user: " + username);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts the claims from the given JWT token.
     *
     * @param token the JWT token
     * @return the claims extracted from the token
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Validates the given JWT token against the provided username.
     *
     * @param token    the JWT token
     * @param username the username to validate against
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}