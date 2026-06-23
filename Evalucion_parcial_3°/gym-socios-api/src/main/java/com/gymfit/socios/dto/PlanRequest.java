package com.gymfit.socios.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequest {

    @NotBlank(message = "El nombre del plan es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El precio mensual es obligatorio")
    @Positive(message = "El precio mensual debe ser mayor a 0")
    private Integer precioMensual;

    @NotNull(message = "La duración en meses es obligatoria")
    @Min(value = 1, message = "La duración mínima es 1 mes")
    private Integer duracionMeses;
}