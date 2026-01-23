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
import java.time.Instant;
import java.util.Date;

import static org.example.validate.JwtValidation.validateClaims;

public class Jwt {
    public static String createToken(User user, PrivateKey privateKey) throws JOSEException {

        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(600);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .audience("next-client")
                .issuer("auth.javajwt.org")
                .subject(user.getUuid())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expiry))
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .build();

        JWSHeader JwtHeader = new JWSHeader.Builder
                (JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                JwtHeader, jwtClaimsSet
        );

        signedJWT.sign(
                new RSASSASigner(privateKey)
        );
        return signedJWT.serialize();
    }

    public static JWTClaimsSet verifyAndGetClaims(
            String token,
            PublicKey publicKey
    ) throws ParseException, JOSEException {

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is missing");
        }

        if (!(publicKey instanceof RSAPublicKey rsaPublicKey)) {
            throw new IllegalArgumentException("Invalid public key type");
        }

        SignedJWT signedJWT = SignedJWT.parse(token);

        RSASSAVerifier verifier = new RSASSAVerifier(rsaPublicKey);
        boolean signatureValid = signedJWT.verify(verifier);

        if (!signatureValid) {
            throw new SecurityException("Invalid JWT signature");
        }

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        validateClaims(claims);

        return claims;
    }

}
