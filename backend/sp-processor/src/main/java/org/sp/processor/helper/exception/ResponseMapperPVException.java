package org.sp.processor.helper.exception;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

public class ResponseMapperPVException implements ResponseExceptionMapper<PVRestClientRuntimeException> {

    @Override
    public PVRestClientRuntimeException toThrowable(Response response) {
        String problem = response.readEntity(String.class);
        int status = response.getStatus();
        if (status == 404) {
            return new ValidationNotFoundException(problem, status);
        }
        return new PVRestClientRuntimeException(problem, status);
    }
}
