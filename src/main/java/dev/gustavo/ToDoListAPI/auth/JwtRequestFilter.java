package dev.gustavo.ToDoListAPI.auth;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.gustavo.ToDoListAPI.utils.JWT.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * The JwtRequestFilter class ensures that each incoming request is checked for a valid JWT token. 
 * If a valid token is found, it extracts the username, loads the user details, and sets the authentication in the security context, allowing the user to access secured endpoints.
 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                username = jwtUtil.extractEmail(jwtToken);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, jwtToken, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(buildErrorResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid auth token!", "Token expired", ex.getMessage()));
        } catch (MalformedJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(buildErrorResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid auth token!", "Token malformed", ex.getMessage()));
        } catch (SignatureException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(buildErrorResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid auth token!", "Invalid token signature", ex.getMessage()));
        } catch (UnsupportedJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(buildErrorResponse(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid auth token!", "Unsupported JWT token", ex.getMessage()));
        }
    }

    // God forgive me for this
    private String buildErrorResponse(int status, String result, String message, String description) {
        return String.format("""
                {
                  "status": %d,
                  "result": "%s",
                  "error": {
                    "code": %d,
                    "message": "%s",
                    "description": "%s"
                  },
                  "data": null
                }""",
                status, result, status, message, description);
    }
}
