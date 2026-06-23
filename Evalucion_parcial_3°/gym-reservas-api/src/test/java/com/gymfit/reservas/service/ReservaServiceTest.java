package com.gymfit.reservas.service;

import com.gymfit.reservas.Client.SocioClient;
import com.gymfit.reservas.Model.ClaseGrupal;
import com.gymfit.reservas.Model.Reserva;
import com.gymfit.reservas.Repository.ClaseGrupalRepository;
import com.gymfit.reservas.Repository.ReservaRepository;
import com.gymfit.reservas.Service.ReservaService;
import com.gymfit.reservas.dto.ApiResponse;
import com.gymfit.reservas.dto.ReservaRequest;
import com.gymfit.reservas.dto.ReservaResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClaseGrupalRepository claseGrupalRepository;

    @Mock
    private SocioClient socioClient;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void crearReserva_deberiaLanzarErrorCuandoClaseNoTieneCupos() {
        ReservaRequest request = new ReservaRequest();
        request.setIdSocio(1L);
        request.setIdClase(10L);

        ClaseGrupal clase = new ClaseGrupal();
        clase.setIdClase(10L);
        clase.setNombre("Spinning");
        clase.setFechaHora(LocalDateTime.now().plusDays(1));
        clase.setCupoMaximo(20);
        clase.setCuposDisponibles(0);
        clase.setActiva(true);

        when(claseGrupalRepository.findById(10L)).thenReturn(Optional.of(clase));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reservaService.crearReserva(request)
        );

        assertEquals("La clase no tiene cupos disponibles", exception.getMessage());

        verify(reservaRepository, never()).save(any(Reserva.class));
        verify(socioClient, never()).verificarMembresiaActiva(anyLong());
    }

    @Test
    void crearReserva_deberiaLanzarErrorCuandoSocioNoTieneMembresiaActiva() {
        ReservaRequest request = new ReservaRequest();
        request.setIdSocio(1L);
        request.setIdClase(10L);

        ClaseGrupal clase = new ClaseGrupal();
        clase.setIdClase(10L);
        clase.setNombre("Funcional");
        clase.setFechaHora(LocalDateTime.now().plusDays(1));
        clase.setCupoMaximo(20);
        clase.setCuposDisponibles(5);
        clase.setActiva(true);

        when(claseGrupalRepository.findById(10L)).thenReturn(Optional.of(clase));
        when(socioClient.verificarMembresiaActiva(1L))
                .thenReturn(new ApiResponse<>(200, "Socio sin membresía activa", false, false));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reservaService.crearReserva(request)
        );

        assertEquals("El socio no tiene membresía activa", exception.getMessage());

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void crearReserva_deberiaGuardarReservaCuandoDatosSonValidos() {
        ReservaRequest request = new ReservaRequest();
        request.setIdSocio(1L);
        request.setIdClase(10L);

        ClaseGrupal clase = new ClaseGrupal();
        clase.setIdClase(10L);
        clase.setNombre("Yoga");
        clase.setFechaHora(LocalDateTime.now().plusDays(1));
        clase.setCupoMaximo(20);
        clase.setCuposDisponibles(5);
        clase.setActiva(true);

        when(claseGrupalRepository.findById(10L)).thenReturn(Optional.of(clase));
        when(socioClient.verificarMembresiaActiva(1L))
                .thenReturn(new ApiResponse<>(200, "Membresía activa", false, true));

        when(reservaRepository.existsByIdSocioAndClaseGrupalIdClaseAndEstado(
                1L, 10L, "CONFIRMADA"
        )).thenReturn(false);

        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva reserva = invocation.getArgument(0);
            reserva.setIdReserva(100L);
            return reserva;
        });

        when(claseGrupalRepository.save(any(ClaseGrupal.class))).thenReturn(clase);

        ReservaResponse response = reservaService.crearReserva(request);

        assertNotNull(response);
        assertEquals(100L, response.getIdReserva());
        assertEquals(1L, response.getIdSocio());
        assertEquals(10L, response.getIdClase());
        assertEquals("Yoga", response.getNombreClase());
        assertEquals("CONFIRMADA", response.getEstado());

        assertEquals(4, clase.getCuposDisponibles());

        verify(reservaRepository, times(1)).save(any(Reserva.class));
        verify(claseGrupalRepository, times(1)).save(clase);
    }
}