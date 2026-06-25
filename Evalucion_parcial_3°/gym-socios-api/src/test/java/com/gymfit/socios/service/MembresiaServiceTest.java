package com.gymfit.socios.service;

import com.gymfit.socios.Model.Membresia;
import com.gymfit.socios.Model.Plan;
import com.gymfit.socios.Model.Socio;
import com.gymfit.socios.Repository.MembresiaRepository;
import com.gymfit.socios.Repository.PlanRepository;
import com.gymfit.socios.Repository.SocioRepository;
import com.gymfit.socios.Service.MembresiaService;
import com.gymfit.socios.dto.MembresiaRequest;
import com.gymfit.socios.dto.MembresiaResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembresiaServiceTest {

    @Mock
    private MembresiaRepository membresiaRepository;

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private MembresiaService membresiaService;

    @Test
    void crearMembresia_deberiaLanzarErrorCuandoFechaFinNoEsPosterior() {
        MembresiaRequest request = new MembresiaRequest();
        request.setIdSocio(1L);
        request.setIdPlan(1L);
        request.setFechaInicio(LocalDate.of(2026, 6, 24));
        request.setFechaFin(LocalDate.of(2026, 6, 20));
        request.setEstado("ACTIVA");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> membresiaService.crearMembresia(request)
        );

        assertEquals("La fecha de fin debe ser posterior a la fecha de inicio", exception.getMessage());

        verify(membresiaRepository, never()).save(any(Membresia.class));
        verify(socioRepository, never()).findById(anyLong());
        verify(planRepository, never()).findById(anyLong());
    }

    @Test
    void crearMembresia_deberiaLanzarErrorCuandoSocioNoExiste() {
        MembresiaRequest request = new MembresiaRequest();
        request.setIdSocio(1L);
        request.setIdPlan(1L);
        request.setFechaInicio(LocalDate.of(2026, 6, 24));
        request.setFechaFin(LocalDate.of(2026, 7, 24));
        request.setEstado("ACTIVA");

        when(socioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> membresiaService.crearMembresia(request)
        );

        assertEquals("Socio no encontrado", exception.getMessage());

        verify(membresiaRepository, never()).save(any(Membresia.class));
        verify(planRepository, never()).findById(anyLong());
    }

    @Test
    void crearMembresia_deberiaGuardarMembresiaCuandoDatosSonValidos() {
        MembresiaRequest request = new MembresiaRequest();
        request.setIdSocio(1L);
        request.setIdPlan(2L);
        request.setFechaInicio(LocalDate.of(2026, 6, 24));
        request.setFechaFin(LocalDate.of(2026, 7, 24));
        request.setEstado("ACTIVA");

        Socio socio = new Socio();
        socio.setIdSocio(1L);
        socio.setNombres("Camila");
        socio.setApellidos("Venegas");

        Plan plan = new Plan();
        plan.setIdPlan(2L);
        plan.setNombre("Plan Mensual");

        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(planRepository.findById(2L)).thenReturn(Optional.of(plan));

        when(membresiaRepository.save(any(Membresia.class))).thenAnswer(invocation -> {
            Membresia membresia = invocation.getArgument(0);
            membresia.setIdMembresia(10L);
            return membresia;
        });

        MembresiaResponse response = membresiaService.crearMembresia(request);

        assertNotNull(response);
        assertEquals(10L, response.getIdMembresia());
        assertEquals("ACTIVA", response.getEstado());
        assertEquals(1L, response.getIdSocio());
        assertEquals("Camila Venegas", response.getNombreSocio());
        assertEquals(2L, response.getIdPlan());
        assertEquals("Plan Mensual", response.getNombrePlan());

        verify(membresiaRepository, times(1)).save(any(Membresia.class));
    }

    @Test
    void verificarMembresiaActiva_deberiaRetornarTrueCuandoExisteActiva() {
        when(membresiaRepository.existsBySocio_IdSocioAndEstado(1L, "ACTIVA"))
                .thenReturn(true);

        boolean resultado = membresiaService.verificarMembresiaActiva(1L);

        assertTrue(resultado);
        verify(membresiaRepository, times(1))
                .existsBySocio_IdSocioAndEstado(1L, "ACTIVA");
    }
}