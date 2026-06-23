package com.gymfit.socios.Service;

import com.gymfit.socios.dto.PlanRequest;
import com.gymfit.socios.dto.PlanResponse;
import com.gymfit.socios.Model.Plan;
import com.gymfit.socios.Repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private static final Logger log = LoggerFactory.getLogger(PlanService.class);

    private final PlanRepository planRepository;


    @Transactional(readOnly = true)
    public List<PlanResponse> listarPlanes() {
        log.info("Listando todos los planes del gimnasio");

        List<Plan> planes = planRepository.findAll();
        List<PlanResponse> respuesta = new ArrayList<>();

        for (Plan plan : planes) {
            respuesta.add(convertirAResponse(plan));
        }

        log.info("Se encontraron {} planes", respuesta.size());
        return respuesta;
    }


    @Transactional(readOnly = true)
    public PlanResponse obtenerPlanPorId(Long idPlan) {
        log.info("Buscando plan con ID: {}", idPlan);

        Plan plan = planRepository.findById(idPlan)
                .orElseThrow(() -> {
                    log.error("Plan no encontrado con ID: {}", idPlan);
                    return new RuntimeException("Plan no encontrado");
                });

        return convertirAResponse(plan);
    }


    public PlanResponse crearPlan(PlanRequest request) {
        log.info("Creando nuevo plan: {}", request.getNombre());

        if (planRepository.existsByNombre(request.getNombre())) {
            log.warn("Ya existe un plan con el nombre: {}", request.getNombre());
            throw new RuntimeException("Ya existe un plan con ese nombre");
        }

        Plan plan = new Plan();
        plan.setNombre(request.getNombre());
        plan.setDescripcion(request.getDescripcion());
        plan.setPrecioMensual(request.getPrecioMensual());
        plan.setDuracionMeses(request.getDuracionMeses());
        plan.setActivo(true);

        Plan planGuardado = planRepository.save(plan);

        log.info("Plan creado correctamente con ID: {}", planGuardado.getIdPlan());

        return convertirAResponse(planGuardado);
    }


    public PlanResponse actualizarPlan(Long idPlan, PlanRequest request) {
        log.info("Actualizando plan con ID: {}", idPlan);

        Plan planExistente = planRepository.findById(idPlan)
                .orElseThrow(() -> {
                    log.error("Plan no encontrado para actualizar, ID: {}", idPlan);
                    return new RuntimeException("Plan no encontrado");
                });

        if (!planExistente.getNombre().equals(request.getNombre())
                && planRepository.existsByNombre(request.getNombre())) {
            log.warn("Nombre de plan duplicado: {}", request.getNombre());
            throw new RuntimeException("Ya existe un plan con ese nombre");
        }

        planExistente.setNombre(request.getNombre());
        planExistente.setDescripcion(request.getDescripcion());
        planExistente.setPrecioMensual(request.getPrecioMensual());
        planExistente.setDuracionMeses(request.getDuracionMeses());

        Plan planActualizado = planRepository.save(planExistente);

        log.info("Plan actualizado correctamente con ID: {}", planActualizado.getIdPlan());

        return convertirAResponse(planActualizado);
    }


    public void eliminarPlan(Long idPlan) {
        log.info("Eliminando plan con ID: {}", idPlan);

        if (!planRepository.existsById(idPlan)) {
            log.error("No se puede eliminar: plan no existe, ID: {}", idPlan);
            throw new RuntimeException("No se puede eliminar: plan no existe");
        }

        planRepository.deleteById(idPlan);

        log.info("Plan eliminado correctamente con ID: {}", idPlan);
    }

    private PlanResponse convertirAResponse(Plan plan) {
        return new PlanResponse(
                plan.getIdPlan(),
                plan.getNombre(),
                plan.getDescripcion(),
                plan.getPrecioMensual(),
                plan.getDuracionMeses()
        );
    }
}