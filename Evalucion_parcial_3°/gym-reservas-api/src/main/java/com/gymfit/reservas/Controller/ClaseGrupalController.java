package com.gymfit.reservas.Controller;

import com.gymfit.reservas.Service.ClaseGrupalService;
import com.gymfit.reservas.dto.ApiResponse;
import com.gymfit.reservas.dto.ClaseGrupalRequest;
import com.gymfit.reservas.dto.ClaseGrupalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/clases")
@RequiredArgsConstructor
public class ClaseGrupalController {

    private final ClaseGrupalService claseGrupalService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClaseGrupalResponse>>> listarClases() {
        List<ClaseGrupalResponse> clases = claseGrupalService.listarClases();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Clases encontradas correctamente",
                false,
                clases
        ));
    }

    @GetMapping("/activas")
    public ResponseEntity<ApiResponse<List<ClaseGrupalResponse>>> listarClasesActivas() {
        List<ClaseGrupalResponse> clases = claseGrupalService.listarClasesActivas();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Clases activas encontradas correctamente",
                false,
                clases
        ));
    }

    @GetMapping("/{idClase}")
    public ResponseEntity<ApiResponse<ClaseGrupalResponse>> obtenerClasePorId(
            @PathVariable Long idClase
    ) {
        ClaseGrupalResponse clase = claseGrupalService.obtenerClasePorId(idClase);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Clase encontrada correctamente",
                false,
                clase
        ));
    }

    @GetMapping("/entrenador/{idEntrenador}")
    public ResponseEntity<ApiResponse<List<ClaseGrupalResponse>>> listarClasesPorEntrenador(
            @PathVariable Long idEntrenador
    ) {
        List<ClaseGrupalResponse> clases =
                claseGrupalService.listarClasesPorEntrenador(idEntrenador);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Clases del entrenador encontradas correctamente",
                false,
                clases
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClaseGrupalResponse>> crearClase(
            @Valid @RequestBody ClaseGrupalRequest request
    ) {
        ClaseGrupalResponse claseCreada = claseGrupalService.crearClase(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Clase creada correctamente",
                false,
                claseCreada
        ));
    }

    @PutMapping("/{idClase}")
    public ResponseEntity<ApiResponse<ClaseGrupalResponse>> actualizarClase(
            @PathVariable Long idClase,
            @Valid @RequestBody ClaseGrupalRequest request
    ) {
        ClaseGrupalResponse claseActualizada =
                claseGrupalService.actualizarClase(idClase, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Clase actualizada correctamente",
                false,
                claseActualizada
        ));
    }

    @DeleteMapping("/{idClase}")
    public ResponseEntity<Void> eliminarClase(@PathVariable Long idClase) {
        claseGrupalService.eliminarClase(idClase);
        return ResponseEntity.noContent().build();
    }
}