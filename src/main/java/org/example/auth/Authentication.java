package org.example.auth;

import org.example.config.UserData;
import org.example.domain.User;

public class Authentication {

    public static User login(String email, String password){
        return UserData.view().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .filter(user -> user.getPassword().equals(password))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("Invalid credentials")
                );
    }
}
