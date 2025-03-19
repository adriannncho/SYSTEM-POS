package org.sp.processor.helper.exception;

import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.logging.Logger;

import java.util.UUID;

@Provider
public class HandlerException implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(HandlerException.class);

    @Inject
    jakarta.inject.Provider<HttpServerRequest> httpServerRequestProvider;

    @Override
    public Response toResponse(Exception exception) {
        return mapExceptionToResponsePVIntegration(exception);
    }

    private Response mapExceptionToResponsePVIntegration(Exception exception) {
        if (exception instanceof SPException ex) {
            ProblemException problemException = ProblemException.builder()
                    .host(httpServerRequestProvider.get().host())
                    .title(ex.getStatus().getReasonPhrase())
                    .detail(ex.getMessage())
                    .uri(httpServerRequestProvider.get().absoluteURI())
                    .errors(ex.getErrors())
                    .build();
            if (problemException.getErrors() != null && !problemException.getErrors().isEmpty()) {
                problemException.setDetail("Error en la solicitud " + httpServerRequestProvider.get().absoluteURI());
            }
            return Response.status(ex.getStatus()).entity(problemException).build();
        }
        if (exception instanceof SPRestClientRuntimeException ex) {
            LOG.errorf(ex, "@mapExceptionToResponsePVIntegration - Error during rest client, status %s, body %s",
                    ex.getStatus().getStatusCode(), ex.getProblem());
            return Response.status(ex.getStatus()).entity(ex.getProblem()).build();
        }
        if (exception instanceof WebApplicationException webApplicationException) {
            Response originalErrorResponse = webApplicationException.getResponse();
            return Response.fromResponse(originalErrorResponse)
                    .entity(exception.getMessage())
                    .build();
        } else {
            UUID idError = UUID.randomUUID();
            LOG.errorf(exception, "@mapExceptionToResponsePVIntegration - Failed to process, " +
                    "idError:%s - request to:%s", idError.toString(), httpServerRequestProvider.get().absoluteURI());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseError(String.valueOf(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()),
                            idError.toString(),
                            "Internal Server Error"))
                    .build();
        }
    }

    public record ResponseError(@Schema(example = "500") String code,
                                @Schema(example = "ef156aec-3d78-4f78-9b69-1dd8154f993d") String idError,
                                @Schema(example = "Internal Server Error") String msg) {
    }
}
