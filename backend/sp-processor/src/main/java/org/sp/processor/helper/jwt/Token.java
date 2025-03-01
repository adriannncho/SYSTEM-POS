package org.sp.processor.helper.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.sp.processor.domain.user.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Token {

    @ConfigProperty(name = "CONSTANTS.PROPERTIES.KEY_TOKEN")
    private static String keyToken;

    @ConfigProperty(name = "CONSTANTS.PROPERTIES.URL_TOKEN")
    private static String issuerToken;

    public static String generateToken(User user) {

        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("SP_" + user.getUserType().getName(), true);

        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.HOURS);

        return JWT.create()
                .withIssuer(issuerToken)
                .withSubject(user.getName())
                .withClaim("document_number", user.getDocumentNumber())
                .withClaim("business_id", user.getBusinessId().getName())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiration))
                .withPayload(customClaims)
                .sign(Algorithm.HMAC256(keyToken));
    }

}
