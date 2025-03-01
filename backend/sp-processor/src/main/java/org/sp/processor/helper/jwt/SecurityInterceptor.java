package org.sp.processor.helper.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.vertx.core.http.HttpHeaders;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Provider
public class SecurityInterceptor implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(SecurityInterceptor.class);

    private static final List<String> EXCLUDED_PATHS = List.of("/sp-processor/login");

    @Inject
    private TokenContext tokenContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();

        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION.toString());

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        if (authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }

        DecodedJWT jwt = extractJsonWebToken(authorizationHeader);
        validateToken(jwt, requestContext);
        tokenContext.setToken(authorizationHeader);
    }

    private void validateToken(DecodedJWT jwt, ContainerRequestContext requestContext) {
        if (jwt == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        if (!isUserSystemPos(jwt)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        if (isTokenExpired(jwt)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private boolean isTokenExpired(DecodedJWT jwt) {
        Date expiration = jwt.getExpiresAt();
        return expiration != null && expiration.toInstant().isBefore(Instant.now());
    }

    private DecodedJWT extractJsonWebToken(String token) {
        token = token.replace("Bearer ", "");

        try {
            return validateTokenCoded(token);
        } catch (JWTDecodeException e) {
            LOG.errorf(e, "@extractJsonWebToken > Error getting token %s", e.getMessage());
            return null;
        }
    }

    private DecodedJWT validateTokenCoded(String token) {
        String secretKey = System.getenv("JWT_SECRET");
        String issuer = System.getenv("JWT_ISSUER");

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            LOG.errorf(e, "@validateToken > Error validating token: %s", e.getMessage());
            return null;
        }
    }

    private boolean isUserSystemPos(DecodedJWT jwt) {
        return jwt.getClaim("groups").asList(String.class).stream().anyMatch(claim -> claim.contains("SP"));
    }

}
