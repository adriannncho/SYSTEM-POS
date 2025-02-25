package org.sp.processor.helper.exception;

import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.stream.Collectors;

@Provider
public class ValidatorsResponseHandlerException implements ExceptionMapper<ConstraintViolationException> {

    @Inject
    jakarta.inject.Provider<HttpServerRequest> httpServerRequestProvider;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ProblemException respuesta = ProblemException.builder()
                .host(httpServerRequestProvider.get().host())
                .title("Solicitud inválida")
                .detail("Error en la solicitud " + httpServerRequestProvider.get().absoluteURI())
                .uri(httpServerRequestProvider.get().absoluteURI())
                .errors(exception.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList()))
                .build();

        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta)
                .type(MediaType.APPLICATION_JSON).build();
    }
}
