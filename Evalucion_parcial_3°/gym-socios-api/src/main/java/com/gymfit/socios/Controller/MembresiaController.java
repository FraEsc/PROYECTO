package com.gymfit.socios.Controller;

import com.gymfit.socios.Service.MembresiaService;
import com.gymfit.socios.dto.ApiResponse;
import com.gymfit.socios.dto.MembresiaRequest;
import com.gymfit.socios.dto.MembresiaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    private final MembresiaService membresiaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MembresiaResponse>>> listarMembresias() {
        List<MembresiaResponse> membresias = membresiaService.listarMembresias();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Membresías encontradas correctamente",
                false,
                membresias
        ));
    }

    @GetMapping("/{idMembresia}")
    public ResponseEntity<ApiResponse<MembresiaResponse>> obtenerMembresiaPorId(
            @PathVariable Long idMembresia
    ) {
        MembresiaResponse membresia = membresiaService.obtenerMembresiaPorId(idMembresia);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Membresía encontrada correctamente",
                false,
                membresia
        ));
    }

    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<ApiResponse<List<MembresiaResponse>>> listarMembresiasPorSocio(
            @PathVariable Long idSocio
    ) {
        List<MembresiaResponse> membresias = membresiaService.listarMembresiasPorSocio(idSocio);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Membresías del socio encontradas correctamente",
                false,
                membresias
        ));
    }

    @GetMapping("/verificar/{idSocio}")
    public ResponseEntity<ApiResponse<Boolean>> verificarMembresiaActiva(
            @PathVariable Long idSocio
    ) {
        boolean tieneActiva = membresiaService.verificarMembresiaActiva(idSocio);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                tieneActiva ? "El socio tiene membresía activa" : "El socio no tiene membresía activa",
                false,
                tieneActiva
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MembresiaResponse>> crearMembresia(
            @Valid @RequestBody MembresiaRequest request
    ) {
        MembresiaResponse membresiaCreada = membresiaService.crearMembresia(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Membresía creada correctamente",
                false,
                membresiaCreada
        ));
    }

    @PutMapping("/{idMembresia}")
    public ResponseEntity<ApiResponse<MembresiaResponse>> actualizarMembresia(
            @PathVariable Long idMembresia,
            @Valid @RequestBody MembresiaRequest request
    ) {
        MembresiaResponse membresiaActualizada =
                membresiaService.actualizarMembresia(idMembresia, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Membresía actualizada correctamente",
                false,
                membresiaActualizada
        ));
    }

    @DeleteMapping("/{idMembresia}")
    public ResponseEntity<Void> eliminarMembresia(@PathVariable Long idMembresia) {
        membresiaService.eliminarMembresia(idMembresia);
        return ResponseEntity.noContent().build();
    }
}