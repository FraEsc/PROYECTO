package com.gymfit.reservas.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "clase_grupal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaseGrupal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Long idClase;


    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;


    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;


    @Column(name = "cupos_disponibles", nullable = false)
    private Integer cuposDisponibles;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;


    @ManyToOne
    @JoinColumn(name = "id_entrenador", nullable = false)
    private Entrenador entrenador;


    @JsonIgnore
    @OneToMany(mappedBy = "claseGrupal")
    private List<Reserva> reservas = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        if (cuposDisponibles == null) {
            cuposDisponibles = cupoMaximo;
        }

        if (activa == null) {
            activa = true;
        }
    }
}