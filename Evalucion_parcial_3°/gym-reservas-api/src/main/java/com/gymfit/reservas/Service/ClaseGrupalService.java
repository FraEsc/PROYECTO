package com.gymfit.reservas.Service;

import com.gymfit.reservas.Model.ClaseGrupal;
import com.gymfit.reservas.Model.Entrenador;
import com.gymfit.reservas.Repository.ClaseGrupalRepository;
import com.gymfit.reservas.Repository.EntrenadorRepository;
import com.gymfit.reservas.dto.ClaseGrupalRequest;
import com.gymfit.reservas.dto.ClaseGrupalResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ClaseGrupalService {

    private static final Logger log = LoggerFactory.getLogger(ClaseGrupalService.class);

    private final ClaseGrupalRepository claseGrupalRepository;
    private final EntrenadorRepository entrenadorRepository;


    @Transactional(readOnly = true)
    public List<ClaseGrupalResponse> listarClases() {
        log.info("Listando todas las clases grupales");

        List<ClaseGrupal> clases = claseGrupalRepository.findAll();
        List<ClaseGrupalResponse> respuesta = new ArrayList<>();

        for (ClaseGrupal clase : clases) {
            respuesta.add(convertirAResponse(clase));
        }

        log.info("Se encontraron {} clases grupales", respuesta.size());
        return respuesta;
    }


    @Transactional(readOnly = true)
    public List<ClaseGrupalResponse> listarClasesActivas() {
        log.info("Listando clases grupales activas");

        List<ClaseGrupal> clases = claseGrupalRepository.findByActivaTrue();
        List<ClaseGrupalResponse> respuesta = new ArrayList<>();

        for (ClaseGrupal clase : clases) {
            respuesta.add(convertirAResponse(clase));
        }

        log.info("Se encontraron {} clases activas", respuesta.size());
        return respuesta;
    }

    @Transactional(readOnly = true)
    public ClaseGrupalResponse obtenerClasePorId(Long idClase) {
        log.info("Buscando clase grupal con ID: {}", idClase);

        ClaseGrupal clase = claseGrupalRepository.findById(idClase)
                .orElseThrow(() -> {
                    log.error("Clase grupal no encontrada con ID: {}", idClase);
                    return new RuntimeException("Clase grupal no encontrada");
                });

        return convertirAResponse(clase);
    }


    @Transactional(readOnly = true)
    public List<ClaseGrupalResponse> listarClasesPorEntrenador(Long idEntrenador) {
        log.info("Listando clases del entrenador ID: {}", idEntrenador);

        if (!entrenadorRepository.existsById(idEntrenador)) {
            log.error("Entrenador no encontrado con ID: {}", idEntrenador);
            throw new RuntimeException("Entrenador no encontrado");
        }

        List<ClaseGrupal> clases = claseGrupalRepository.findByEntrenadorIdEntrenador(idEntrenador);
        List<ClaseGrupalResponse> respuesta = new ArrayList<>();

        for (ClaseGrupal clase : clases) {
            respuesta.add(convertirAResponse(clase));
        }

        log.info("Se encontraron {} clases para el entrenador ID: {}", respuesta.size(), idEntrenador);
        return respuesta;
    }

    public ClaseGrupalResponse crearClase(ClaseGrupalRequest request) {
        log.info("Creando clase grupal: {}", request.getNombre());

        if (request.getCupoMaximo() <= 0) {
            log.warn("Cupo máximo inválido: {}", request.getCupoMaximo());
            throw new RuntimeException("El cupo máximo debe ser mayor a cero");
        }

        Entrenador entrenador = entrenadorRepository.findById(request.getIdEntrenador())
                .orElseThrow(() -> {
                    log.error("Entrenador no encontrado con ID: {}", request.getIdEntrenador());
                    return new RuntimeException("Entrenador no encontrado");
                });

        ClaseGrupal clase = new ClaseGrupal();
        clase.setNombre(request.getNombre());
        clase.setDescripcion(request.getDescripcion());
        clase.setFechaHora(request.getFechaHora());
        clase.setCupoMaximo(request.getCupoMaximo());
        clase.setCuposDisponibles(request.getCupoMaximo());
        clase.setActiva(true);
        clase.setEntrenador(entrenador);

        ClaseGrupal claseGuardada = claseGrupalRepository.save(clase);

        log.info("Clase grupal creada correctamente con ID: {}", claseGuardada.getIdClase());

        return convertirAResponse(claseGuardada);
    }


    public ClaseGrupalResponse actualizarClase(Long idClase, ClaseGrupalRequest request) {
        log.info("Actualizando clase grupal con ID: {}", idClase);

        ClaseGrupal claseExistente = claseGrupalRepository.findById(idClase)
                .orElseThrow(() -> {
                    log.error("Clase grupal no encontrada para actualizar, ID: {}", idClase);
                    return new RuntimeException("Clase grupal no encontrada");
                });

        if (request.getCupoMaximo() <= 0) {
            log.warn("Cupo máximo inválido al actualizar: {}", request.getCupoMaximo());
            throw new RuntimeException("El cupo máximo debe ser mayor a cero");
        }

        Entrenador entrenador = entrenadorRepository.findById(request.getIdEntrenador())
                .orElseThrow(() -> {
                    log.error("Entrenador no encontrado con ID: {}", request.getIdEntrenador());
                    return new RuntimeException("Entrenador no encontrado");
                });

        claseExistente.setNombre(request.getNombre());
        claseExistente.setDescripcion(request.getDescripcion());
        claseExistente.setFechaHora(request.getFechaHora());
        claseExistente.setCupoMaximo(request.getCupoMaximo());
        claseExistente.setEntrenador(entrenador);


        if (claseExistente.getCuposDisponibles() == null) {
            claseExistente.setCuposDisponibles(request.getCupoMaximo());
        }

        ClaseGrupal claseActualizada = claseGrupalRepository.save(claseExistente);

        log.info("Clase grupal actualizada correctamente con ID: {}", claseActualizada.getIdClase());

        return convertirAResponse(claseActualizada);
    }


    public void eliminarClase(Long idClase) {
        log.info("Eliminando clase grupal con ID: {}", idClase);

        if (!claseGrupalRepository.existsById(idClase)) {
            log.error("No se puede eliminar: clase grupal no existe, ID: {}", idClase);
            throw new RuntimeException("No se puede eliminar: clase grupal no existe");
        }

        claseGrupalRepository.deleteById(idClase);

        log.info("Clase grupal eliminada correctamente con ID: {}", idClase);
    }


    private ClaseGrupalResponse convertirAResponse(ClaseGrupal clase) {
        return new ClaseGrupalResponse(
                clase.getIdClase(),
                clase.getNombre(),
                clase.getDescripcion(),
                clase.getFechaHora(),
                clase.getCupoMaximo(),
                clase.getCuposDisponibles(),
                clase.getActiva(),
                clase.getEntrenador().getIdEntrenador(),
                clase.getEntrenador().getNombres() + " " + clase.getEntrenador().getApellidos()
        );
    }
}