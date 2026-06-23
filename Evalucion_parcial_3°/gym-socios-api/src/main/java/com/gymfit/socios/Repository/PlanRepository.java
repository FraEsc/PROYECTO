package com.gymfit.socios.Repository;
import com.gymfit.socios.Model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    boolean existsByNombre(String nombre);
}