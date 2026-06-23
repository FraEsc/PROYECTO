package com.gymfit.socios.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "membresia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membresia {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membresia")
    private Long idMembresia;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;


    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;


    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;


    @ManyToOne
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;


    @PrePersist
    public void prePersist() {
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now();
        }

        if (estado == null) {
            estado = "ACTIVA";
        }
    }
}

