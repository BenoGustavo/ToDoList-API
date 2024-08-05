package dev.gustavo.ToDoListAPI.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.gustavo.ToDoListAPI.models.UserModel;
import dev.gustavo.ToDoListAPI.repositories.interfaces.IUserRepository;

/*
This service is responsible for loading user-specific data during the authentication process. Here's a breakdown of what this service does:

Dependency Injection:

The IUserRepository is injected into the service using the @Autowired annotation. This repository is used to interact with the database to retrieve user information.
loadUserByUsername Method:

This method is overridden from the UserDetailsService interface.
It takes an email (username) as a parameter and attempts to find a user with that email in the database using the userRepository.
If no user is found, it throws a UsernameNotFoundException.
If a user is found, it returns a UserDetails object. This object is created using the user's email, hashed password, and an empty list of authorities (roles/permissions).
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convert roles to GrantedAuthority
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getHashedPassword(),
                authorities);
    }
}