package com.gymfit.socios.service;

import com.gymfit.socios.Model.Socio;
import com.gymfit.socios.Repository.SocioRepository;
import com.gymfit.socios.Service.SocioService;
import com.gymfit.socios.dto.SocioRequest;
import com.gymfit.socios.dto.SocioResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SocioServiceTest {

    @Mock
    private SocioRepository socioRepository;

    @InjectMocks
    private SocioService socioService;

    @Test
    void registrarSocio_deberiaGuardarSocioCuandoRutYCorreoNoExisten() {
        SocioRequest request = new SocioRequest();
        request.setRut("12345678-9");
        request.setNombres("Camila");
        request.setApellidos("Rojas");
        request.setCorreo("camila@test.cl");
        request.setTelefono("912345678");

        when(socioRepository.existsByRut("12345678-9")).thenReturn(false);
        when(socioRepository.existsByCorreo("camila@test.cl")).thenReturn(false);

        when(socioRepository.save(any(Socio.class))).thenAnswer(invocation -> {
            Socio socio = invocation.getArgument(0);
            socio.setIdSocio(1L);
            return socio;
        });

        SocioResponse response = socioService.registrarSocio(request);

        assertNotNull(response);
        assertEquals(1L, response.getIdSocio());
        assertEquals("12345678-9", response.getRut());
        assertEquals("Camila", response.getNombres());
        assertEquals("Rojas", response.getApellidos());
        assertEquals("camila@test.cl", response.getCorreo());

        verify(socioRepository, times(1)).save(any(Socio.class));
    }

    @Test
    void registrarSocio_deberiaLanzarErrorCuandoRutYaExiste() {
        SocioRequest request = new SocioRequest();
        request.setRut("12345678-9");
        request.setNombres("Camila");
        request.setApellidos("Rojas");
        request.setCorreo("camila@test.cl");
        request.setTelefono("912345678");

        when(socioRepository.existsByRut("12345678-9")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> socioService.registrarSocio(request)
        );

        assertEquals("Ya existe un socio con ese RUT", exception.getMessage());

        verify(socioRepository, never()).save(any(Socio.class));
    }

    @Test
    void registrarSocio_deberiaLanzarErrorCuandoCorreoYaExiste() {
        SocioRequest request = new SocioRequest();
        request.setRut("12345678-9");
        request.setNombres("Camila");
        request.setApellidos("Rojas");
        request.setCorreo("camila@test.cl");
        request.setTelefono("912345678");

        when(socioRepository.existsByRut("12345678-9")).thenReturn(false);
        when(socioRepository.existsByCorreo("camila@test.cl")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> socioService.registrarSocio(request)
        );

        assertEquals("Ya existe un socio con ese correo", exception.getMessage());

        verify(socioRepository, never()).save(any(Socio.class));
    }
}