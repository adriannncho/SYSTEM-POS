package org.sp.processor.helper.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

import java.util.List;

@Getter
public class SPException extends RuntimeException {
    private final Response.Status status;

    private List<String> errors;

    public SPException(int statusCode, String msg) {
        super(msg);
        this.status = Response.Status.fromStatusCode(statusCode);
    }

    public SPException(int statusCode, String msg, List<String> errors) {
        super(msg);
        this.errors = errors;
        this.status = Response.Status.fromStatusCode(statusCode);
    }
}
