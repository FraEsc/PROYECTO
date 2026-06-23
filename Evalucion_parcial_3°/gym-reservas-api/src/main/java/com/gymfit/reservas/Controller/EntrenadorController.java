package com.gymfit.reservas.Controller;

import com.gymfit.reservas.Service.EntrenadorService;
import com.gymfit.reservas.dto.ApiResponse;
import com.gymfit.reservas.dto.EntrenadorRequest;
import com.gymfit.reservas.dto.EntrenadorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/entrenadores")
@RequiredArgsConstructor
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EntrenadorResponse>>> listarEntrenadores() {
        List<EntrenadorResponse> entrenadores = entrenadorService.listarEntrenadores();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Entrenadores encontrados correctamente",
                false,
                entrenadores
        ));
    }

    @GetMapping("/{idEntrenador}")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> obtenerEntrenadorPorId(
            @PathVariable Long idEntrenador
    ) {
        EntrenadorResponse entrenador = entrenadorService.obtenerEntrenadorPorId(idEntrenador);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Entrenador encontrado correctamente",
                false,
                entrenador
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EntrenadorResponse>> crearEntrenador(
            @Valid @RequestBody EntrenadorRequest request
    ) {
        EntrenadorResponse entrenadorCreado = entrenadorService.crearEntrenador(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Entrenador creado correctamente",
                false,
                entrenadorCreado
        ));
    }

    @PutMapping("/{idEntrenador}")
    public ResponseEntity<ApiResponse<EntrenadorResponse>> actualizarEntrenador(
            @PathVariable Long idEntrenador,
            @Valid @RequestBody EntrenadorRequest request
    ) {
        EntrenadorResponse entrenadorActualizado =
                entrenadorService.actualizarEntrenador(idEntrenador, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Entrenador actualizado correctamente",
                false,
                entrenadorActualizado
        ));
    }

    @DeleteMapping("/{idEntrenador}")
    public ResponseEntity<Void> eliminarEntrenador(@PathVariable Long idEntrenador) {
        entrenadorService.eliminarEntrenador(idEntrenador);
        return ResponseEntity.noContent().build();
    }
}