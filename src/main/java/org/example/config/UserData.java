package org.example.config;

import org.example.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    private static final List<User> users = new ArrayList<>();

    static {
        users.add(create("u001", "user1@mail.com", "123456", "ADMIN"));
        users.add(create("u002", "user2@mail.com", "123456", "USER"));
        users.add(create("u003", "user3@mail.com", "123456", "USER"));
        users.add(create("u004", "user4@mail.com", "123456", "USER"));
        users.add(create("u005", "user5@mail.com", "123456", "USER"));
        users.add(create("u006", "user6@mail.com", "123456", "USER"));
        users.add(create("u007", "user7@mail.com", "123456", "MODERATOR"));
        users.add(create("u008", "user8@mail.com", "123456", "USER"));
        users.add(create("u009", "user9@mail.com", "123456", "USER"));
        users.add(create("u010", "user10@mail.com", "123456", "ADMIN"));
    }

    public static List<User> view() {
        return List.copyOf(users);
    }

    public static User createUser(String uuid, String email, String password, String role) {
        User user = create(uuid, email, password, role);
        users.add(user);
        return user;
    }

    private static User create(String uuid, String email, String password, String role) {
        User user = new User();
        user.setUuid(uuid);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
