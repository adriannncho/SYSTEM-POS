package org.sp.processor.domain.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDTO {

    @NotNull(message = "El campo 'businessId' (id de negocio) no debe estar vacío")
    @Positive(message = "El ID del negocio debe ser un número positivo")
    private Long businessId;

    @NotNull(message = "El campo 'tableNumber' (número de mesa) no debe estar vacío")
    @Positive(message = "El número de mesa debe ser un número positivo")
    private Long tableNumber;

    @NotNull(message = "El campo 'userId' (id de usuario) no debe estar vacío")
    @Positive(message = "El ID del usuario debe ser un número positivo")
    private Long userId;

    @NotNull(message = "La lista 'detailsOrders' no debe estar vacía")
    @Valid
    private List<DetailOrderDTO> detailsOrders;
}
