package com.gymfit.reservas.Repository;

import com.gymfit.reservas.Model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByIdSocio(Long idSocio);

    List<Reserva> findByClaseGrupalIdClase(Long idClase);

    boolean existsByIdSocioAndClaseGrupalIdClaseAndEstado(Long idSocio, Long idClase, String estado);
}