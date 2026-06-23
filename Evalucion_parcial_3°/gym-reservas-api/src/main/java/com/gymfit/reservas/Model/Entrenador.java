package com.gymfit.reservas.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "entrenador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Entrenador {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrenador")
    private Long idEntrenador;


    @Column(name = "rut", nullable = false, length = 13, unique = true)
    private String rut;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;


    @Column(name = "especialidad", nullable = false, length = 100)
    private String especialidad;


    @Column(name = "correo", nullable = false, length = 150, unique = true)
    private String correo;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "entrenador")
    private List<ClaseGrupal> clasesGrupales = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (activo == null) {
            activo = true;
        }
    }
}