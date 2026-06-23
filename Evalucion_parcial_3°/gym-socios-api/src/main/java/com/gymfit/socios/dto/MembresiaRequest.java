package com.gymfit.socios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembresiaRequest {

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
            regexp = "ACTIVA|VENCIDA|CANCELADA",
            message = "El estado debe ser: ACTIVA, VENCIDA o CANCELADA"
    )
    private String estado;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long idSocio;

    @NotNull(message = "El ID del plan es obligatorio")
    private Long idPlan;
}