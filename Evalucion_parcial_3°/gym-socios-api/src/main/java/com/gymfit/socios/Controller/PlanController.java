package com.gymfit.socios.Controller;

import com.gymfit.socios.Service.PlanService;
import com.gymfit.socios.dto.ApiResponse;
import com.gymfit.socios.dto.PlanRequest;
import com.gymfit.socios.dto.PlanResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/planes")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlanResponse>>> listarPlanes() {
        List<PlanResponse> planes = planService.listarPlanes();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Planes encontrados correctamente",
                false,
                planes
        ));
    }

    @GetMapping("/{idPlan}")
    public ResponseEntity<ApiResponse<PlanResponse>> obtenerPlanPorId(@PathVariable Long idPlan) {
        PlanResponse plan = planService.obtenerPlanPorId(idPlan);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Plan encontrado correctamente",
                false,
                plan
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlanResponse>> crearPlan(
            @Valid @RequestBody PlanRequest request
    ) {
        PlanResponse planCreado = planService.crearPlan(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Plan creado correctamente",
                false,
                planCreado
        ));
    }

    @PutMapping("/{idPlan}")
    public ResponseEntity<ApiResponse<PlanResponse>> actualizarPlan(
            @PathVariable Long idPlan,
            @Valid @RequestBody PlanRequest request
    ) {
        PlanResponse planActualizado = planService.actualizarPlan(idPlan, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Plan actualizado correctamente",
                false,
                planActualizado
        ));
    }

    @DeleteMapping("/{idPlan}")
    public ResponseEntity<Void> eliminarPlan(@PathVariable Long idPlan) {
        planService.eliminarPlan(idPlan);
        return ResponseEntity.noContent().build();
    }
}