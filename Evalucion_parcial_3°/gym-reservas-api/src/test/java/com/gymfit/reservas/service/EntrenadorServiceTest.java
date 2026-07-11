package com.gymfit.reservas.service;

import com.gymfit.reservas.Model.Entrenador;
import com.gymfit.reservas.Repository.EntrenadorRepository;
import com.gymfit.reservas.Service.EntrenadorService;
import com.gymfit.reservas.dto.EntrenadorRequest;
import com.gymfit.reservas.dto.EntrenadorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntrenadorServiceTest {

    @Mock
    private EntrenadorRepository entrenadorRepository;

    @InjectMocks
    private EntrenadorService entrenadorService;


    private Entrenador crearEntrenador() {
        Entrenador entrenador = new Entrenador();

        entrenador.setIdEntrenador(1L);
        entrenador.setRut("11.111.111-1");
        entrenador.setNombres("Carlos");
        entrenador.setApellidos("Pérez");
        entrenador.setEspecialidad("Fitness");
        entrenador.setCorreo("carlos@gymfit.cl");
        entrenador.setActivo(true);

        return entrenador;
    }


    private EntrenadorRequest crearRequest() {
        EntrenadorRequest request = new EntrenadorRequest();

        request.setRut("11.111.111-1");
        request.setNombres("Carlos");
        request.setApellidos("Pérez");
        request.setEspecialidad("Fitness");
        request.setCorreo("carlos@gymfit.cl");

        return request;
    }


    @Test
    void listarEntrenadores_deberiaRetornarEntrenadoresRegistrados() {

        // Given
        Entrenador entrenador = crearEntrenador();

        when(entrenadorRepository.findAll())
                .thenReturn(List.of(entrenador));

        // When
        List<EntrenadorResponse> respuesta =
                entrenadorService.listarEntrenadores();

        // Then
        assertEquals(1, respuesta.size());
        assertEquals("Carlos", respuesta.get(0).getNombres());
        assertEquals(1L, respuesta.get(0).getIdEntrenador());

        verify(entrenadorRepository, times(1))
                .findAll();
    }


    @Test
    void obtenerEntrenadorPorId_deberiaRetornarEntrenadorCuandoExiste() {

        // Given
        Entrenador entrenador = crearEntrenador();

        when(entrenadorRepository.findById(1L))
                .thenReturn(Optional.of(entrenador));

        // When
        EntrenadorResponse respuesta =
                entrenadorService.obtenerEntrenadorPorId(1L);

        // Then
        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getIdEntrenador());
        assertEquals("Carlos", respuesta.getNombres());
        assertEquals("Fitness", respuesta.getEspecialidad());
    }


    @Test
    void obtenerEntrenadorPorId_deberiaLanzarErrorCuandoNoExiste() {

        // Given
        when(entrenadorRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entrenadorService.obtenerEntrenadorPorId(99L)
        );

        // Then
        assertEquals(
                "Entrenador no encontrado",
                exception.getMessage()
        );

        verify(entrenadorRepository, times(1))
                .findById(99L);
    }


    @Test
    void crearEntrenador_deberiaGuardarEntrenadorCuandoDatosSonValidos() {

        // Given
        EntrenadorRequest request = crearRequest();

        when(entrenadorRepository.existsByRut(request.getRut()))
                .thenReturn(false);

        when(entrenadorRepository.existsByCorreo(request.getCorreo()))
                .thenReturn(false);

        when(entrenadorRepository.save(any(Entrenador.class)))
                .thenAnswer(invocation -> {
                    Entrenador entrenador = invocation.getArgument(0);
                    entrenador.setIdEntrenador(1L);
                    return entrenador;
                });

        // When
        EntrenadorResponse respuesta =
                entrenadorService.crearEntrenador(request);

        // Then
        assertNotNull(respuesta);
        assertEquals(1L, respuesta.getIdEntrenador());
        assertEquals("Carlos", respuesta.getNombres());
        assertEquals("carlos@gymfit.cl", respuesta.getCorreo());
        assertTrue(respuesta.getActivo());

        verify(entrenadorRepository, times(1))
                .save(any(Entrenador.class));
    }


    @Test
    void crearEntrenador_deberiaLanzarErrorCuandoRutEstaDuplicado() {

        // Given
        EntrenadorRequest request = crearRequest();

        when(entrenadorRepository.existsByRut(request.getRut()))
                .thenReturn(true);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entrenadorService.crearEntrenador(request)
        );

        // Then
        assertEquals(
                "Ya existe un entrenador con ese RUT",
                exception.getMessage()
        );

        verify(entrenadorRepository, never())
                .save(any(Entrenador.class));
    }


    @Test
    void crearEntrenador_deberiaLanzarErrorCuandoCorreoEstaDuplicado() {

        // Given
        EntrenadorRequest request = crearRequest();

        when(entrenadorRepository.existsByRut(request.getRut()))
                .thenReturn(false);

        when(entrenadorRepository.existsByCorreo(request.getCorreo()))
                .thenReturn(true);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entrenadorService.crearEntrenador(request)
        );

        // Then
        assertEquals(
                "Ya existe un entrenador con ese correo",
                exception.getMessage()
        );

        verify(entrenadorRepository, never())
                .save(any(Entrenador.class));
    }


    @Test
    void actualizarEntrenador_deberiaActualizarEntrenadorCuandoDatosSonValidos() {

        // Given
        Entrenador entrenador = crearEntrenador();
        EntrenadorRequest request = crearRequest();

        request.setNombres("Carlos Andrés");
        request.setEspecialidad("Spinning");

        when(entrenadorRepository.findById(1L))
                .thenReturn(Optional.of(entrenador));

        when(entrenadorRepository.save(any(Entrenador.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        EntrenadorResponse respuesta =
                entrenadorService.actualizarEntrenador(1L, request);

        // Then
        assertEquals("Carlos Andrés", respuesta.getNombres());
        assertEquals("Spinning", respuesta.getEspecialidad());

        verify(entrenadorRepository, times(1))
                .save(entrenador);
    }


    @Test
    void actualizarEntrenador_deberiaLanzarErrorCuandoNoExiste() {

        // Given
        EntrenadorRequest request = crearRequest();

        when(entrenadorRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entrenadorService.actualizarEntrenador(99L, request)
        );

        // Then
        assertEquals(
                "Entrenador no encontrado",
                exception.getMessage()
        );

        verify(entrenadorRepository, never())
                .save(any(Entrenador.class));
    }


    @Test
    void actualizarEntrenador_deberiaLanzarErrorCuandoNuevoRutEstaDuplicado() {

        // Given
        Entrenador entrenador = crearEntrenador();
        EntrenadorRequest request = crearRequest();

        request.setRut("22.222.222-2");

        when(entrenadorRepository.findById(1L))
                .thenReturn(Optional.of(entrenador));

        when(entrenadorRepository.existsByRut(request.getRut()))
                .thenReturn(true);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entrenadorService.actualizarEntrenador(1L, request)
        );

        // Then
        assertEquals(
                "Ya existe un entrenador con ese RUT",
                exception.getMessage()
        );

        verify(entrenadorRepository, never())
                .save(any(Entrenador.class));
    }


    @Test
    void actualizarEntrenador_deberiaLanzarErrorCuandoNuevoCorreoEstaDuplicado() {

        // Given
        Entrenador entrenador = crearEntrenador();
        EntrenadorRequest request = crearRequest();

        request.setCorreo("otro@gymfit.cl");

        when(entrenadorRepository.findById(1L))
                .thenReturn(Optional.of(entrenador));

        when(entrenadorRepository.existsByCorreo(request.getCorreo()))
                .thenReturn(true);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entrenadorService.actualizarEntrenador(1L, request)
        );

        // Then
        assertEquals(
                "Ya existe un entrenador con ese correo",
                exception.getMessage()
        );

        verify(entrenadorRepository, never())
                .save(any(Entrenador.class));
    }


    @Test
    void eliminarEntrenador_deberiaEliminarEntrenadorCuandoExiste() {

        // Given
        when(entrenadorRepository.existsById(1L))
                .thenReturn(true);

        // When
        entrenadorService.eliminarEntrenador(1L);

        // Then
        verify(entrenadorRepository, times(1))
                .deleteById(1L);
    }


    @Test
    void eliminarEntrenador_deberiaLanzarErrorCuandoNoExiste() {

        // Given
        when(entrenadorRepository.existsById(99L))
                .thenReturn(false);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> entrenadorService.eliminarEntrenador(99L)
        );

        // Then
        assertEquals(
                "No se puede eliminar: entrenador no existe",
                exception.getMessage()
        );

        verify(entrenadorRepository, never())
                .deleteById(anyLong());
    }
}