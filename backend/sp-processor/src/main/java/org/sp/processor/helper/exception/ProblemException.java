package org.sp.processor.helper.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@Builder
public class ProblemException {
    @Schema(example = "localhost:8080")
    private String host;
    @Schema(example = "Solicitud inválida")
    private String title;
    @Schema(example = "Error en la solicitud http://localhost:8080/integration/API_PATH")
    private String detail;
    @Schema(example = "http://localhost:8080/integration/API_PATH")
    private String uri;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private List<String> errors;
}
