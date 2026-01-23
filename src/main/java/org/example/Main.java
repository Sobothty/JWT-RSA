package org.example;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.example.auth.Authentication;
import org.example.config.LoaderKeys;
import org.example.config.UserData;
import org.example.domain.User;
import org.example.jwt.Jwt;

import java.security.*;
import java.text.ParseException;
import java.util.Scanner;


public class Main {

    private static String currentToken;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        PrivateKey privateKey = LoaderKeys.loadingPrivateKey("keys/jwt_private_key.pem");
        PublicKey publicKey = LoaderKeys.loadPublicKey("keys/jwt_public_key.pem");

        while (true) {
            printMenu();

            int choice = readChoice(scanner);
            switch (choice) {
                case 1 -> viewUsers();
                case 2 -> handleLogin(scanner, privateKey);
                case 3 -> handleVerifyToken(scanner,publicKey);
                case 4 -> exit();
                default -> System.out.println("Invalid option");
            }
        }
    }

    //  ==================Handle Manu============================
    private static void printMenu() {
        System.out.println("\n===== JWT TEST MENU =====");
        System.out.println("1. View users");
        System.out.println("2. Login user (generate JWT)");
        System.out.println("3. Verify token");
        System.out.println("4. Exit");
        System.out.print("Choose option: ");
    }

    private static int readChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    // =====================Login=========================
    private static void handleLogin(Scanner scanner, PrivateKey privateKey) {

        if (currentToken != null) {
            System.out.println("Already logged in. Please restart to login again.");
            return;
        }

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (username.isBlank() || password.isBlank()) {
            System.out.println("Username and password are required");
            return;
        }

        try {
            User user = Authentication.login(username, password);
            currentToken = Jwt.createToken(user, privateKey);

            System.out.println("\nLogin successful");
            System.out.println("Generated JWT:\n" + currentToken);

        } catch (RuntimeException | JOSEException e) {
            System.out.println("\nLogin failed: invalid credentials");
        }
    }

    private static void handleVerifyToken(Scanner scanner, PublicKey publicKey) {
        System.out.println("Enter JWT token: ");
        String currentTokens = scanner.nextLine().trim();

        try {
            Jwt.verifyAndGetClaims(currentTokens, publicKey);

            System.out.println("Token is valid");

            printJwtDetails(currentTokens);
        } catch (RuntimeException | ParseException | JOSEException e) {
            System.out.println("Token verification failed");
        }
    }

    //  ==========================View User=========================
    public static void viewUsers() {
        System.out.println("\n----- USERS -----");
        UserData.view().forEach(u ->
                System.out.println("email: " + u.getEmail() + ", role: " + u.getRole())
        );
    }

    //  ============================= Exit =========================
    private static void exit() {
        System.out.println("Exiting program...");
        System.exit(0);
    }

    //    ========================== Print Token ======================
    public static void printJwtDetails(String token) throws ParseException {

        SignedJWT jwt = SignedJWT.parse(token);

        // ----- HEADER -----
        System.out.println("\n----- HEADER -----");
        JWSHeader header = jwt.getHeader();
        System.out.println("Algorithm: " + header.getAlgorithm());
        header.toJSONObject().forEach(
                (k, v) -> System.out.println(k + " : " + v)
        );

        // ----- PAYLOAD -----
        System.out.println("\n----- PAYLOAD -----");
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        claims.getClaims().forEach(
                (k, v) -> System.out.println(k + " : " + v)
        );

        // ----- SIGNATURE -----
        System.out.println("\n----- SIGNATURE -----");
        System.out.println("Present: " + (jwt.getSignature() != null));
    }

}