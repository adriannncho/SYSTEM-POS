package org.sp.processor.domain.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDTO {

    @NotNull(message = "El campo del documento no puede ser nulo ni vacío.")
    private Long document;

    @NotEmpty(message = "El campo de la contraseña no puede ser nulo ni vacío.")
    private String password;
}
