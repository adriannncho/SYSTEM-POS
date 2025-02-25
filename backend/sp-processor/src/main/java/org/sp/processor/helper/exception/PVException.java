package org.sp.processor.helper.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.List;

@Getter
public class PVException extends RuntimeException {
    private final Response.Status status;

    private List<String> errors;

    public PVException(int statusCode, String msg) {
        super(msg);
        this.status = Response.Status.fromStatusCode(statusCode);
    }

    public PVException(int statusCode, String msg, List<String> errors) {
        super(msg);
        this.errors = errors;
        this.status = Response.Status.fromStatusCode(statusCode);
    }
}
