package org.sp.processor.helper.jwt;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

public class ClientSecurityInterceptor implements ClientRequestFilter {

    @Inject
    private TokenContext tokenContext;

    @Override
    public void filter(ClientRequestContext requestContext) {
        if (!requestContext.getHeaders().containsKey("Authorization")) {
            requestContext.getHeaders().add("Authorization", "Bearer " + tokenContext.getToken());
        }
    }
}
