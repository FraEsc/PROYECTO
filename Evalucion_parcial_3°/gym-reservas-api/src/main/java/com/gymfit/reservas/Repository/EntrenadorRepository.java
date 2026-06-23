package com.gymfit.reservas.Repository;

import com.gymfit.reservas.Model.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {

    boolean existsByRut(String rut);

    boolean existsByCorreo(String correo);
}