package com.gymfit.reservas.Controller;

import com.gymfit.reservas.Service.ReservaService;
import com.gymfit.reservas.dto.ApiResponse;
import com.gymfit.reservas.dto.ReservaRequest;
import com.gymfit.reservas.dto.ReservaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservaResponse>>> listarReservas() {
        List<ReservaResponse> reservas = reservaService.listarReservas();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Reservas encontradas correctamente",
                false,
                reservas
        ));
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<ApiResponse<ReservaResponse>> obtenerReservaPorId(
            @PathVariable Long idReserva
    ) {
        ReservaResponse reserva = reservaService.obtenerReservaPorId(idReserva);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Reserva encontrada correctamente",
                false,
                reserva
        ));
    }

    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<ApiResponse<List<ReservaResponse>>> listarReservasPorSocio(
            @PathVariable Long idSocio
    ) {
        List<ReservaResponse> reservas = reservaService.listarReservasPorSocio(idSocio);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Reservas del socio encontradas correctamente",
                false,
                reservas
        ));
    }

    @GetMapping("/clase/{idClase}")
    public ResponseEntity<ApiResponse<List<ReservaResponse>>> listarReservasPorClase(
            @PathVariable Long idClase
    ) {
        List<ReservaResponse> reservas = reservaService.listarReservasPorClase(idClase);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Reservas de la clase encontradas correctamente",
                false,
                reservas
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservaResponse>> crearReserva(
            @Valid @RequestBody ReservaRequest request
    ) {
        ReservaResponse reservaCreada = reservaService.crearReserva(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Reserva creada correctamente",
                false,
                reservaCreada
        ));
    }

    @PutMapping("/{idReserva}/cancelar")
    public ResponseEntity<ApiResponse<ReservaResponse>> cancelarReserva(
            @PathVariable Long idReserva
    ) {
        ReservaResponse reservaCancelada = reservaService.cancelarReserva(idReserva);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Reserva cancelada correctamente",
                false,
                reservaCancelada
        ));
    }

    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long idReserva) {
        reservaService.eliminarReserva(idReserva);
        return ResponseEntity.noContent().build();
    }
}