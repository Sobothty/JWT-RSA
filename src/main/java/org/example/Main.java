package org.example;

import org.example.auth.Authentication;
import org.example.config.LoaderKeys;
import org.example.config.UserData;
import org.example.domain.User;
import org.example.jwt.Jwt;
import java.security.*;
import java.util.Scanner;

public class Main {

    private static String currentToken = null;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        PrivateKey privateKey = LoaderKeys.loadingPrivateKey("keys/jwt_private_key.pem");
        PublicKey publicKey = LoaderKeys.loadPublicKey("keys/jwt_public_key.pem");

        while (true) {
            System.out.println("\n===== JWT TEST MENU =====");
            System.out.println("1. View users");
            System.out.println("2. Login user (generate JWT)");
            System.out.println("3. Verify token");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (choice) {

                case 1 -> viewUsers();

                case 2 -> {
                    System.out.print("Username: ");
                    String username = sc.nextLine();

                    System.out.print("Password: ");
                    String password = sc.nextLine();

                    User user = Authentication.login(username, password);

                    if (user == null) {
                        System.out.println("Login failed");
                    } else {
                        currentToken = Jwt.createToken(user, privateKey);
                        System.out.println("\nLogin success");
                        System.out.println("Generated JWT:\n" + currentToken);
                    }
                }

                case 3 -> {
                    if (currentToken == null) {
                        System.out.println("No token found. Please login first.");
                    } else {
                        Jwt.verifyToken(currentToken, publicKey);
                    }
                }

                case 4 -> {
                    System.out.println("Exiting program...");
                    return;
                }

                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void viewUsers() {
        System.out.println("\n----- USERS -----");
        for (User u : UserData.view()) {
            System.out.println("email: " + u.getEmail() + ", Role: " + u.getRole());
        }
    }
}