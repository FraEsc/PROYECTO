package com.gymfit.reservas.service;

import com.gymfit.reservas.Model.ClaseGrupal;
import com.gymfit.reservas.Model.Entrenador;
import com.gymfit.reservas.Repository.ClaseGrupalRepository;
import com.gymfit.reservas.Repository.EntrenadorRepository;
import com.gymfit.reservas.Service.ClaseGrupalService;
import com.gymfit.reservas.dto.ClaseGrupalRequest;
import com.gymfit.reservas.dto.ClaseGrupalResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClaseGrupalServiceTest {

    @Mock
    private ClaseGrupalRepository claseGrupalRepository;

    @Mock
    private EntrenadorRepository entrenadorRepository;

    @InjectMocks
    private ClaseGrupalService claseGrupalService;


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


    private ClaseGrupal crearClase() {
        ClaseGrupal clase = new ClaseGrupal();
        clase.setIdClase(10L);
        clase.setNombre("Spinning");
        clase.setDescripcion("Clase de spinning");
        clase.setFechaHora(LocalDateTime.of(2026, 7, 11, 18, 0));
        clase.setCupoMaximo(20);
        clase.setCuposDisponibles(15);
        clase.setActiva(true);
        clase.setEntrenador(crearEntrenador());

        return clase;
    }


    private ClaseGrupalRequest crearRequest() {
        ClaseGrupalRequest request = new ClaseGrupalRequest();
        request.setNombre("Spinning");
        request.setDescripcion("Clase de spinning");
        request.setFechaHora(LocalDateTime.of(2026, 7, 11, 18, 0));
        request.setCupoMaximo(20);
        request.setIdEntrenador(1L);

        return request;
    }


    @Test
    void listarClases_deberiaRetornarClasesRegistradas() {


        ClaseGrupal clase = crearClase();

        when(claseGrupalRepository.findAll())
                .thenReturn(List.of(clase));


        List<ClaseGrupalResponse> respuesta =
                claseGrupalService.listarClases();


        assertEquals(1, respuesta.size());
        assertEquals("Spinning", respuesta.get(0).getNombre());
        assertEquals(10L, respuesta.get(0).getIdClase());

        verify(claseGrupalRepository, times(1)).findAll();
    }


    @Test
    void listarClasesActivas_deberiaRetornarSoloClasesActivas() {


        ClaseGrupal clase = crearClase();

        when(claseGrupalRepository.findByActivaTrue())
                .thenReturn(List.of(clase));


        List<ClaseGrupalResponse> respuesta =
                claseGrupalService.listarClasesActivas();


        assertEquals(1, respuesta.size());
        assertTrue(respuesta.get(0).getActiva());

        verify(claseGrupalRepository, times(1))
                .findByActivaTrue();
    }


    @Test
    void obtenerClasePorId_deberiaRetornarClaseCuandoExiste() {


        ClaseGrupal clase = crearClase();

        when(claseGrupalRepository.findById(10L))
                .thenReturn(Optional.of(clase));


        ClaseGrupalResponse respuesta =
                claseGrupalService.obtenerClasePorId(10L);


        assertNotNull(respuesta);
        assertEquals(10L, respuesta.getIdClase());
        assertEquals("Spinning", respuesta.getNombre());
    }


    @Test
    void obtenerClasePorId_deberiaLanzarErrorCuandoNoExiste() {


        when(claseGrupalRepository.findById(99L))
                .thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> claseGrupalService.obtenerClasePorId(99L)
        );


        assertEquals(
                "Clase grupal no encontrada",
                exception.getMessage()
        );
    }


    @Test
    void listarClasesPorEntrenador_deberiaRetornarClasesDelEntrenador() {


        ClaseGrupal clase = crearClase();

        when(entrenadorRepository.existsById(1L))
                .thenReturn(true);

        when(claseGrupalRepository
                .findByEntrenadorIdEntrenador(1L))
                .thenReturn(List.of(clase));


        List<ClaseGrupalResponse> respuesta =
                claseGrupalService.listarClasesPorEntrenador(1L);


        assertEquals(1, respuesta.size());
        assertEquals(1L, respuesta.get(0).getIdEntrenador());
        assertEquals(
                "Carlos Pérez",
                respuesta.get(0).getNombreEntrenador()
        );
    }


    @Test
    void listarClasesPorEntrenador_deberiaLanzarErrorCuandoEntrenadorNoExiste() {


        when(entrenadorRepository.existsById(99L))
                .thenReturn(false);


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> claseGrupalService.listarClasesPorEntrenador(99L)
        );


        assertEquals(
                "Entrenador no encontrado",
                exception.getMessage()
        );

        verify(
                claseGrupalRepository,
                never()
        ).findByEntrenadorIdEntrenador(anyLong());
    }


    @Test
    void crearClase_deberiaGuardarClaseCuandoDatosSonValidos() {


        ClaseGrupalRequest request = crearRequest();
        Entrenador entrenador = crearEntrenador();

        when(entrenadorRepository.findById(1L))
                .thenReturn(Optional.of(entrenador));

        when(claseGrupalRepository.save(any(ClaseGrupal.class)))
                .thenAnswer(invocation -> {
                    ClaseGrupal clase = invocation.getArgument(0);
                    clase.setIdClase(10L);
                    return clase;
                });


        ClaseGrupalResponse respuesta =
                claseGrupalService.crearClase(request);


        assertNotNull(respuesta);
        assertEquals(10L, respuesta.getIdClase());
        assertEquals("Spinning", respuesta.getNombre());
        assertEquals(20, respuesta.getCupoMaximo());
        assertEquals(20, respuesta.getCuposDisponibles());
        assertTrue(respuesta.getActiva());

        verify(claseGrupalRepository, times(1))
                .save(any(ClaseGrupal.class));
    }


    @Test
    void crearClase_deberiaLanzarErrorCuandoCupoMaximoEsInvalido() {


        ClaseGrupalRequest request = crearRequest();
        request.setCupoMaximo(0);


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> claseGrupalService.crearClase(request)
        );


        assertEquals(
                "El cupo máximo debe ser mayor a cero",
                exception.getMessage()
        );

        verify(claseGrupalRepository, never())
                .save(any(ClaseGrupal.class));
    }


    @Test
    void actualizarClase_deberiaMantenerLosCuposUtilizados() {


        ClaseGrupal clase = crearClase();

        ClaseGrupalRequest request = crearRequest();
        request.setCupoMaximo(25);

        when(claseGrupalRepository.findById(10L))
                .thenReturn(Optional.of(clase));

        when(entrenadorRepository.findById(1L))
                .thenReturn(Optional.of(crearEntrenador()));

        when(claseGrupalRepository.save(any(ClaseGrupal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        ClaseGrupalResponse respuesta =
                claseGrupalService.actualizarClase(10L, request);


        assertEquals(25, respuesta.getCupoMaximo());

        assertEquals(
                20,
                respuesta.getCuposDisponibles()
        );

        verify(claseGrupalRepository, times(1))
                .save(clase);
    }


    @Test
    void actualizarClase_deberiaLanzarErrorCuandoCupoEsMenorAReservasExistentes() {


        ClaseGrupal clase = crearClase();

        clase.setCupoMaximo(20);
        clase.setCuposDisponibles(5);

        ClaseGrupalRequest request = crearRequest();
        request.setCupoMaximo(10);

        when(claseGrupalRepository.findById(10L))
                .thenReturn(Optional.of(clase));


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> claseGrupalService.actualizarClase(10L, request)
        );


        assertEquals(
                "El cupo máximo no puede ser menor a las reservas existentes",
                exception.getMessage()
        );

        verify(claseGrupalRepository, never())
                .save(any(ClaseGrupal.class));
    }


    @Test
    void eliminarClase_deberiaEliminarClaseCuandoExiste() {


        when(claseGrupalRepository.existsById(10L))
                .thenReturn(true);


        claseGrupalService.eliminarClase(10L);


        verify(claseGrupalRepository, times(1))
                .deleteById(10L);
    }


    @Test
    void eliminarClase_deberiaLanzarErrorCuandoNoExiste() {


        when(claseGrupalRepository.existsById(99L))
                .thenReturn(false);


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> claseGrupalService.eliminarClase(99L)
        );


        assertEquals(
                "No se puede eliminar: clase grupal no existe",
                exception.getMessage()
        );

        verify(claseGrupalRepository, never())
                .deleteById(anyLong());
    }
    @Test
    void actualizarClase_deberiaLanzarErrorCuandoClaseNoExiste() {


        ClaseGrupalRequest request = crearRequest();

        when(claseGrupalRepository.findById(99L))
                .thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> claseGrupalService.actualizarClase(99L, request)
        );


        assertEquals(
                "Clase grupal no encontrada",
                exception.getMessage()
        );

        verify(claseGrupalRepository, times(1))
                .findById(99L);

        verify(claseGrupalRepository, never())
                .save(any(ClaseGrupal.class));
    }
}