package org.example.auth;

import org.example.config.UserData;
import org.example.domain.User;

public class Authentication {

    public static User login(String email, String password) {

        User user = UserData.view().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Email not found")
                );

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
