package com.gymfit.socios.Repository;

import com.gymfit.socios.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    boolean existsByRut(String rut);

    boolean existsByCorreo(String correo);

    Optional<Socio> findByRut(String rut);
}
