package org.sp.processor.domain.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class ProductDTO {

    @NotNull(message = "El campo del Id no debe estar vacío")
    private int idProduct;

    @NotNull(message = "El campo name (nombre) no puede ser nulo o estar vacío.")
    @Schema(example = "TRUCHA ALBARDADA")
    private String name;

    @NotNull(message = "El campo description (descripción) no puede ser nulo o estar vacío.")
    private String description;

    @NotNull(message = "El campo value (precio) no puede ser nulo o estar vacío.")
    @Positive(message = "El campo value (precio) no puede ser igual o menor a cero.")
    private Long value;

    @NotNull(message = "El campo idCategory (categoria) no puede ser nulo o estar vacío.")
    private int idCategory;

    @NotNull(message = "El campo de ESTADO no puede ser nulo o esta vacío")
    private boolean status;
}
