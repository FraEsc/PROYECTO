package com.gymfit.socios.dto;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembresiaResponse {

    private Long idMembresia;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private Long idSocio;
    private String nombreSocio;
    private Long idPlan;
    private String nombrePlan;
}