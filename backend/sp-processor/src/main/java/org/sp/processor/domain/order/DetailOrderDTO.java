package org.sp.processor.domain.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DetailOrderDTO {

    @NotNull(message = "El campo productId (Id del producto) no debe estar vacío")
    @Positive(message = "El ID del producto debe ser un número positivo")
    private Long productId;

    @NotNull(message = "El campo quantity (cantidad) no debe estar vacío")
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    @Positive(message = "La cantidad debe ser un número positivo")
    private Long quantity;

    @Size(max = 255, message = "La observación no debe superar los 255 caracteres")
    private String observation;

}
