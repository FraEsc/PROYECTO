package com.gymfit.reservas.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;


    @Column(name = "id_socio", nullable = false)
    private Long idSocio;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    private ClaseGrupal claseGrupal;


    @PrePersist
    public void prePersist() {
        if (fechaReserva == null) {
            fechaReserva = LocalDateTime.now();
        }

        if (estado == null) {
            estado = "CONFIRMADA";
        }
    }
}