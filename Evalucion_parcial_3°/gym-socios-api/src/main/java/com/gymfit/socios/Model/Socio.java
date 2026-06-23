package com.gymfit.socios.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "socio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    private Long idSocio;

    @Column(name = "rut", nullable = false, length = 13, unique = true)
    private String rut;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;


    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;


    @Column(name = "correo", nullable = false, length = 150, unique = true)
    private String correo;


    @Column(name = "telefono", length = 20)
    private String telefono;


    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;


    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @JsonIgnore
    @OneToMany(mappedBy = "socio")
    private List<Membresia> membresias = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }

        if (activo == null) {
            activo = true;
        }
    }
}