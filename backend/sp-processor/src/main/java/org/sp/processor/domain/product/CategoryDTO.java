package org.sp.processor.domain.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CategoryDTO {

    @NotNull(message = "El campo id no puede ser nulo o estar vacío")
    @Positive(message = "El campo id no puede ser igual o menor a cero.")
    private int idCategory;

    @NotNull(message = "El campo name no puede ser nulo o estar vacío")
    private String name;
}
