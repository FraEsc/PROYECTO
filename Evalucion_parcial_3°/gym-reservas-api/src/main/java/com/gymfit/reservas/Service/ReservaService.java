package com.gymfit.reservas.Service;

import com.gymfit.reservas.Client.SocioClient;
import com.gymfit.reservas.Model.ClaseGrupal;
import com.gymfit.reservas.Model.Reserva;
import com.gymfit.reservas.Repository.ClaseGrupalRepository;
import com.gymfit.reservas.Repository.ReservaRepository;
import com.gymfit.reservas.dto.ApiResponse;
import com.gymfit.reservas.dto.ReservaRequest;
import com.gymfit.reservas.dto.ReservaResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final ClaseGrupalRepository claseGrupalRepository;
    private final SocioClient socioClient;

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservas() {
        log.info("Listando todas las reservas");

        List<Reserva> reservas = reservaRepository.findAll();
        List<ReservaResponse> respuesta = new ArrayList<>();

        for (Reserva reserva : reservas) {
            respuesta.add(convertirAResponse(reserva));
        }

        log.info("Se encontraron {} reservas", respuesta.size());
        return respuesta;
    }

    @Transactional(readOnly = true)
    public ReservaResponse obtenerReservaPorId(Long idReserva) {
        log.info("Buscando reserva con ID: {}", idReserva);

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> {
                    log.error("Reserva no encontrada con ID: {}", idReserva);
                    return new RuntimeException("Reserva no encontrada");
                });

        return convertirAResponse(reserva);
    }


    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservasPorSocio(Long idSocio) {
        log.info("Listando reservas del socio ID: {}", idSocio);

        List<Reserva> reservas = reservaRepository.findByIdSocio(idSocio);
        List<ReservaResponse> respuesta = new ArrayList<>();

        for (Reserva reserva : reservas) {
            respuesta.add(convertirAResponse(reserva));
        }

        log.info("Se encontraron {} reservas para el socio ID: {}", respuesta.size(), idSocio);
        return respuesta;
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservasPorClase(Long idClase) {
        log.info("Listando reservas de la clase ID: {}", idClase);

        if (!claseGrupalRepository.existsById(idClase)) {
            log.error("Clase grupal no encontrada con ID: {}", idClase);
            throw new RuntimeException("Clase grupal no encontrada");
        }

        List<Reserva> reservas = reservaRepository.findByClaseGrupalIdClase(idClase);
        List<ReservaResponse> respuesta = new ArrayList<>();

        for (Reserva reserva : reservas) {
            respuesta.add(convertirAResponse(reserva));
        }

        log.info("Se encontraron {} reservas para la clase ID: {}", respuesta.size(), idClase);
        return respuesta;
    }


    public ReservaResponse crearReserva(ReservaRequest request) {
        log.info("Creando reserva para socio ID: {} en clase ID: {}",
                request.getIdSocio(), request.getIdClase());

        ClaseGrupal clase = claseGrupalRepository.findById(request.getIdClase())
                .orElseThrow(() -> {
                    log.error("Clase grupal no encontrada con ID: {}", request.getIdClase());
                    return new RuntimeException("Clase grupal no encontrada");
                });

        if (!Boolean.TRUE.equals(clase.getActiva())) {
            log.warn("Intento de reserva en clase inactiva. Clase ID: {}", request.getIdClase());
            throw new RuntimeException("No se puede reservar una clase inactiva");
        }

        if (clase.getCuposDisponibles() == null || clase.getCuposDisponibles() <= 0) {
            log.warn("Clase sin cupos disponibles. Clase ID: {}", request.getIdClase());
            throw new RuntimeException("La clase no tiene cupos disponibles");
        }


        ApiResponse<Boolean> respuestaMembresia = socioClient.verificarMembresiaActiva(request.getIdSocio());

        if (respuestaMembresia == null || !Boolean.TRUE.equals(respuestaMembresia.getData())) {
            log.warn("Socio ID: {} no tiene membresía activa", request.getIdSocio());
            throw new RuntimeException("El socio no tiene membresía activa");
        }

        boolean yaTieneReserva = reservaRepository.existsByIdSocioAndClaseGrupalIdClaseAndEstado(
                request.getIdSocio(),
                request.getIdClase(),
                "CONFIRMADA"
        );

        if (yaTieneReserva) {
            log.warn("El socio ID: {} ya tiene reserva confirmada para la clase ID: {}",
                    request.getIdSocio(), request.getIdClase());
            throw new RuntimeException("El socio ya tiene una reserva confirmada para esta clase");
        }

        Reserva reserva = new Reserva();
        reserva.setIdSocio(request.getIdSocio());
        reserva.setClaseGrupal(clase);
        reserva.setFechaReserva(LocalDateTime.now());
        reserva.setEstado("CONFIRMADA");

        clase.setCuposDisponibles(clase.getCuposDisponibles() - 1);

        Reserva reservaGuardada = reservaRepository.save(reserva);
        claseGrupalRepository.save(clase);

        log.info("Reserva creada correctamente con ID: {}", reservaGuardada.getIdReserva());

        return convertirAResponse(reservaGuardada);
    }


    public ReservaResponse cancelarReserva(Long idReserva) {
        log.info("Cancelando reserva con ID: {}", idReserva);

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> {
                    log.error("Reserva no encontrada con ID: {}", idReserva);
                    return new RuntimeException("Reserva no encontrada");
                });

        if ("CANCELADA".equals(reserva.getEstado())) {
            log.warn("La reserva ID: {} ya se encuentra cancelada", idReserva);
            throw new RuntimeException("La reserva ya se encuentra cancelada");
        }

        if ("CONFIRMADA".equals(reserva.getEstado())) {
            ClaseGrupal clase = reserva.getClaseGrupal();
            clase.setCuposDisponibles(clase.getCuposDisponibles() + 1);
            claseGrupalRepository.save(clase);
        }

        reserva.setEstado("CANCELADA");

        Reserva reservaCancelada = reservaRepository.save(reserva);

        log.info("Reserva cancelada correctamente con ID: {}", idReserva);

        return convertirAResponse(reservaCancelada);
    }


    public void eliminarReserva(Long idReserva) {
        log.info("Eliminando reserva con ID: {}", idReserva);

        if (!reservaRepository.existsById(idReserva)) {
            log.error("No se puede eliminar: reserva no existe, ID: {}", idReserva);
            throw new RuntimeException("No se puede eliminar: reserva no existe");
        }

        reservaRepository.deleteById(idReserva);

        log.info("Reserva eliminada correctamente con ID: {}", idReserva);
    }


    private ReservaResponse convertirAResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getIdReserva(),
                reserva.getIdSocio(),
                reserva.getClaseGrupal().getIdClase(),
                reserva.getClaseGrupal().getNombre(),
                reserva.getClaseGrupal().getFechaHora(),
                reserva.getFechaReserva(),
                reserva.getEstado()
        );
    }
}
