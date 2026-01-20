package org.example.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

public class Jwt {
    public static String createJwt(JWTClaimsSet claimsSet, RSAPrivateKey privateKey) throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(new RSASSASigner(privateKey));
        return signedJWT.serialize();
    }

    public static boolean verifyJwt(String token, RSAPublicKey publicKey) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(token);
        return jwt.verify(new RSASSAVerifier(publicKey));
    }
}
