package com.gymfit.reservas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaseGrupalRequest {

    @NotBlank(message = "El nombre de la clase es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha y hora de la clase es obligatoria")
    private LocalDateTime fechaHora;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo máximo debe ser al menos 1")
    private Integer cupoMaximo;

    @NotNull(message = "El ID del entrenador es obligatorio")
    private Long idEntrenador;
}