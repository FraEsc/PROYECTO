package com.gymfit.reservas.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntrenadorRequest {

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 8, max = 13, message = "El RUT debe tener entre 8 y 13 caracteres")
    private String rut;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
    private String apellidos;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres")
    private String especialidad;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
    private String correo;
}