package com.gymfit.socios.Controller;


import com.gymfit.socios.Service.SocioService;
import com.gymfit.socios.dto.ApiResponse;
import com.gymfit.socios.dto.SocioRequest;
import com.gymfit.socios.dto.SocioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SocioResponse>>> listarSocios() {
        List<SocioResponse> socios = socioService.listarSocios();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Socios encontrados correctamente",
                false,
                socios
        ));
    }

    @GetMapping("/{idSocio}")
    public ResponseEntity<ApiResponse<SocioResponse>> obtenerSocioPorId(@PathVariable Long idSocio) {
        SocioResponse socio = socioService.obtenerSocioPorId(idSocio);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Socio encontrado correctamente",
                false,
                socio
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SocioResponse>> registrarSocio(
            @Valid @RequestBody SocioRequest request
    ) {
        SocioResponse socioRegistrado = socioService.registrarSocio(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Socio registrado correctamente",
                false,
                socioRegistrado
        ));
    }

    @PutMapping("/{idSocio}")
    public ResponseEntity<ApiResponse<SocioResponse>> actualizarSocio(
            @PathVariable Long idSocio,
            @Valid @RequestBody SocioRequest request
    ) {
        SocioResponse socioActualizado = socioService.actualizarSocio(idSocio, request);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Socio actualizado correctamente",
                false,
                socioActualizado
        ));
    }

    @DeleteMapping("/{idSocio}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long idSocio) {
        socioService.eliminarSocio(idSocio);
        return ResponseEntity.noContent().build();
    }
}