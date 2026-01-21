package org.example.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.example.domain.User;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class Jwt {
    public static String createToken(User user, PrivateKey privateKey) throws JOSEException {

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUuid())
                .issuer("http://localhost:8080")
                .issueTime(new Date())
                .expirationTime(Date.from(new Date().toInstant().plusSeconds(3600)))
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256), jwtClaimsSet
        );

        signedJWT.sign(
                new RSASSASigner(privateKey)
        );
        return signedJWT.serialize();
    }

    public static void verifyToken(String token, PublicKey publicKey) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);

        boolean isValidToken = jwt.verify(new RSASSAVerifier((RSAPublicKey) publicKey));

        // header verification
        JWSHeader header = jwt.getHeader();
        System.out.println("\n----- HEADER -----");
        System.out.println("Algorithm: " + header.getAlgorithm());
        System.out.println("Raw JSON: " + header.toJSONObject());

        // payload verification
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        System.out.println("\n----- PAYLOAD -----");
        for (Map.Entry<String, Object> entry : claims.getClaims().entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // signature verification
        System.out.println("\n----- SIGNATURE -----");
        System.out.println("Verified: " + isValidToken + "");
    }
}
