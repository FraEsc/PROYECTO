package com.gymfit.reservas.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntrenadorResponse {

    private Long idEntrenador;
    private String rut;
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String correo;
    private Boolean activo;
}