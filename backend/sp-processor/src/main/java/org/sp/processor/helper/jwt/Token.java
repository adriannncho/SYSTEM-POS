package org.sp.processor.helper.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.sp.processor.domain.user.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Token {

    public static String generateToken(User user, String keyToken, String issuerToken) {

        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("groups", List.of("SP_" + user.getUserType().getName()));

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
