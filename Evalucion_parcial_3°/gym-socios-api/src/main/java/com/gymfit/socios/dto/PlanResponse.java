package com.gymfit.socios.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {

    private Long idPlan;
    private String nombre;
    private String descripcion;
    private Integer precioMensual;
    private Integer duracionMeses;
}