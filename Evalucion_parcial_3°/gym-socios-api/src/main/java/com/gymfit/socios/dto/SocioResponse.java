package com.gymfit.socios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocioResponse {

    private Long idSocio;
    private String rut;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
}