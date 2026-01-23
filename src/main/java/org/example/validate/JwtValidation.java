package org.example.validate;

import com.nimbusds.jwt.JWTClaimsSet;

import java.time.Instant;

public class JwtValidation {
    public static void validateClaims(JWTClaimsSet claims) {

        Instant now = Instant.now();

        if (claims.getExpirationTime() == null ||
                claims.getExpirationTime().toInstant().isBefore(now)) {
            throw new SecurityException("Token has expired");
        }

        if (claims.getNotBeforeTime() != null &&
                claims.getNotBeforeTime().toInstant().isAfter(now)) {
            throw new SecurityException("Token not yet valid");
        }

        if (!"auth.javajwt.org".equals(claims.getIssuer())) {
            throw new SecurityException("Invalid token issuer");
        }

        if (!claims.getAudience().contains("next-client")) {
            throw new SecurityException("Invalid token audience");
        }

        if (claims.getSubject() == null || claims.getSubject().isBlank()) {
            throw new SecurityException("Invalid subject");
        }
    }

}
