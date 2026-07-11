package com.gymfit.reservas.Controller;

import com.gymfit.reservas.Service.ReservaService;
import com.gymfit.reservas.dto.ApiResponse;
import com.gymfit.reservas.dto.ReservaRequest;
import com.gymfit.reservas.dto.ReservaResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Tag(
        name = "Reservas",
        description = "Endpoints para gestionar las reservas de clases grupales de GymFit"
)
public class ReservaController {

    private final ReservaService reservaService;

    @Operation(
            summary = "Listar todas las reservas",
            description = "Obtiene todas las reservas registradas en GymFit"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Reservas encontradas correctamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })

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
    @Operation(
            summary = "Obtener una reserva por ID",
            description = "Busca una reserva utilizando su identificador"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Reserva encontrada correctamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada"
            )
    })
    @GetMapping("/{idReserva}")
    public ResponseEntity<ApiResponse<ReservaResponse>> obtenerReservaPorId(
            @Parameter(
                    description = "ID de la reserva",
                    example = "1"
            )
            @PathVariable Long idReserva
    ) {
        ReservaResponse reserva = reservaService.obtenerReservaPorId(idReserva);

        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Reserva encontrada correctamente",
                false,
                reserva
        ));
    }

    @Operation(
            summary = "Listar reservas de un socio",
            description = "Obtiene todas las reservas asociadas al identificador de un socio"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Reservas del socio encontradas correctamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<ApiResponse<List<ReservaResponse>>> listarReservasPorSocio(
            @Parameter(
                    description = "ID del socio",
                    example = "1"
            )
            @PathVariable Long idSocio
    ) {
        List<ReservaResponse> reservas = reservaService.listarReservasPorSocio(idSocio);

        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Reservas del socio encontradas correctamente",
                false,
                reservas
        ));
    }

    @Operation(
            summary = "Listar reservas de una clase",
            description = "Obtiene todas las reservas asociadas a una clase grupal"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Reservas de la clase encontradas correctamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @GetMapping("/clase/{idClase}")
    public ResponseEntity<ApiResponse<List<ReservaResponse>>> listarReservasPorClase(
            @Parameter(
                    description = "ID de la clase grupal",
                    example = "1"
            )
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
    @Operation(
            summary = "Crear una reserva",
            description = "Crea una reserva después de validar la clase, los cupos disponibles y la membresía activa del socio mediante el microservicio de socios"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Reserva creada correctamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o regla de negocio incumplida"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "503",
                    description = "El microservicio de socios no se encuentra disponible"
            )
    })
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
    @Operation(
            summary = "Cancelar una reserva",
            description = "Cancela una reserva confirmada y devuelve un cupo disponible a la clase grupal"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Reserva cancelada correctamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "La reserva no puede ser cancelada"
            )
    })
    @PutMapping("/{idReserva}/cancelar")
    public ResponseEntity<ApiResponse<ReservaResponse>> cancelarReserva(
            @Parameter(
                    description = "ID de la reserva que se desea cancelar",
                    example = "1"
            )
            @PathVariable Long idReserva
    ) {
        ReservaResponse reservaCancelada =
                reservaService.cancelarReserva(idReserva);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Reserva cancelada correctamente",
                false,
                reservaCancelada
        ));
    }

    @Operation(
            summary = "Eliminar una reserva",
            description = "Elimina una reserva. Si se encuentra confirmada, devuelve el cupo disponible a la clase grupal"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Reserva eliminada correctamente"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada"
            )
    })
    @DeleteMapping("/{idReserva}")
    public ResponseEntity<Void> eliminarReserva(
            @Parameter(
                    description = "ID de la reserva que se desea eliminar",
                    example = "1"
            )
            @PathVariable Long idReserva
    ) {
        reservaService.eliminarReserva(idReserva);

        return ResponseEntity.noContent().build();
    }
}