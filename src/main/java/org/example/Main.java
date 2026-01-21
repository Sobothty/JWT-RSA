package org.example;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.example.jwt.Jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, JOSEException, ParseException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Instant now = Instant.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("https://api.example.com")
                .subject("Test User")
                .audience("demo user")
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(600)))
                .claim("email", "krysobothty@gmail.com")
                .claim("role", "admin")
                .build();

        String token = Jwt.createJwt(claimsSet, privateKey);
        System.out.println("Generated token: " + token);
        System.out.println("Public key: " + publicKey);

        boolean verifyToken = Jwt.verifyJwt(token, publicKey);
        System.out.println("Token verified: " + verifyToken);
    }
}