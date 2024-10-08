package dev.gustavo.ToDoListAPI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.gustavo.ToDoListAPI.auth.JwtRequestFilter;
import dev.gustavo.ToDoListAPI.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        private static final String[] SWAGGER_WHITELIST = {
                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources"
        };

        final static String[] ALLOWED_ROUTES_WITHOUT_AUTH = {
                        "/auth/**", "/users/", "/", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
                        "/swagger-resources", "/swagger-ui.html",
        };
        final static String[] ONLY_ADMIN_ALLOWED_ROUTES = {
                        "/users/admin/**", "/taskbundles/admin/**", "/tasks/admin/**"
        };
        final static String[] ONLY_COMMON_ALLOWED_ROUTES = {
                        "/common/**",
        };

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                                                .requestMatchers(ALLOWED_ROUTES_WITHOUT_AUTH).permitAll()
                                                .requestMatchers(ONLY_ADMIN_ALLOWED_ROUTES).hasRole("ADMIN")
                                                .requestMatchers(ONLY_COMMON_ALLOWED_ROUTES).hasRole("COMMON")
                                                .anyRequest().authenticated())
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);

                authenticationManagerBuilder
                                .userDetailsService(customUserDetailsService)
                                .passwordEncoder(passwordEncoder());

                return authenticationManagerBuilder.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(@NonNull CorsRegistry registry) {
                                registry.addMapping("/**")
                                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                                .allowedOrigins("*")
                                                .allowedHeaders("*");
                        }
                };
        }
}
