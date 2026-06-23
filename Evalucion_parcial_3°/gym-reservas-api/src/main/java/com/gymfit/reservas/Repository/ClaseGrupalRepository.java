package com.gymfit.reservas.Repository;

import com.gymfit.reservas.Model.ClaseGrupal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaseGrupalRepository extends JpaRepository<ClaseGrupal, Long> {

    List<ClaseGrupal> findByEntrenadorIdEntrenador(Long idEntrenador);

    List<ClaseGrupal> findByActivaTrue();
}