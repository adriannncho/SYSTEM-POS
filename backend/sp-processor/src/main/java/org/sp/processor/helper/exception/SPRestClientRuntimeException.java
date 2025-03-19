package org.sp.processor.helper.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public class SPRestClientRuntimeException extends RuntimeException {

    private final Response.Status status;
    private final String problem;

    public SPRestClientRuntimeException(String problem, int status) {
        super("Body: " + problem + " status: " + status);
        this.status = Response.Status.fromStatusCode(status);
        this.problem = problem;
    }
}