package org.sp.processor.helper.exception;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.jboss.logging.Logger;

public class ResponseMapperException implements ResponseExceptionMapper<RuntimeException> {

    private static final Logger LOG = Logger.getLogger(ResponseMapperException.class);

    @Override
    public RuntimeException toThrowable(Response response) {

        String body = "-";
        int status = response.getStatus();

        try {
            response.bufferEntity();
            if (response.hasEntity()) {
                body = response.readEntity(String.class);
            }
        } catch (IllegalStateException e) {
            body = "@toThrowable,failed to read entity stream";
            LOG.error(body, e);
        } catch (ProcessingException e) {
            body = "@,toThrowable,could not buffer stream";
            LOG.error(body, e);
        }

        return new RuntimeException("@toThrowable-ResponseMapperException,An error was generated in " +
                "rest client:status:" + status + ":body:" + body);
    }
}
