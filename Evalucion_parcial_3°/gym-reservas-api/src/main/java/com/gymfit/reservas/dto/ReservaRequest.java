package com.gymfit.reservas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequest {

    @NotNull(message = "El ID del socio es obligatorio")
    private Long idSocio;

    @NotNull(message = "El ID de la clase es obligatorio")
    private Long idClase;
}