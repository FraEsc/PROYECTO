package com.gymfit.reservas.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponse {

    private Long idReserva;
    private Long idSocio;
    private Long idClase;
    private String nombreClase;
    private LocalDateTime fechaHoraClase;
    private LocalDateTime fechaReserva;
    private String estado;
}