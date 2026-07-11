package com.gymfit.socios.Repository;

import com.gymfit.socios.Model.Membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MembresiaRepository extends JpaRepository<Membresia, Long> {

    List<Membresia> findBySocio_IdSocio(Long idSocio);

    boolean existsBySocio_IdSocioAndEstado(
            Long idSocio,
            String estado
    );

    boolean existsBySocio_IdSocioAndEstadoAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            Long idSocio,
            String estado,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
