package org.sp.processor.domain.product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class CategorySaveDTO {

    @NotBlank(message = "El campo name (nombre) no puede ser nulo o estar vacío.")
    @Schema(example = "Bebidas Naturales")
    private String name;
}
