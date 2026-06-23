package com.gymfit.reservas.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaseGrupalResponse {

    private Long idClase;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaHora;
    private Integer cupoMaximo;
    private Integer cuposDisponibles;
    private Boolean activa;
    private Long idEntrenador;
    private String nombreEntrenador;
}