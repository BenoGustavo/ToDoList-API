package dev.gustavo.ToDoListAPI.utils;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        return PATTERN.matcher(email).matches();
    }
}