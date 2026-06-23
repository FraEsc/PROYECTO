package com.gymfit.reservas.Service;

import com.gymfit.reservas.Model.Entrenador;
import com.gymfit.reservas.Repository.EntrenadorRepository;
import com.gymfit.reservas.dto.EntrenadorRequest;
import com.gymfit.reservas.dto.EntrenadorResponse;
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
public class EntrenadorService {

    private static final Logger log = LoggerFactory.getLogger(EntrenadorService.class);

    private final EntrenadorRepository entrenadorRepository;


    @Transactional(readOnly = true)
    public List<EntrenadorResponse> listarEntrenadores() {
        log.info("Listando todos los entrenadores");

        List<Entrenador> entrenadores = entrenadorRepository.findAll();
        List<EntrenadorResponse> respuesta = new ArrayList<>();

        for (Entrenador entrenador : entrenadores) {
            respuesta.add(convertirAResponse(entrenador));
        }

        log.info("Se encontraron {} entrenadores", respuesta.size());
        return respuesta;
    }


    @Transactional(readOnly = true)
    public EntrenadorResponse obtenerEntrenadorPorId(Long idEntrenador) {
        log.info("Buscando entrenador con ID: {}", idEntrenador);

        Entrenador entrenador = entrenadorRepository.findById(idEntrenador)
                .orElseThrow(() -> {
                    log.error("Entrenador no encontrado con ID: {}", idEntrenador);
                    return new RuntimeException("Entrenador no encontrado");
                });

        return convertirAResponse(entrenador);
    }


    public EntrenadorResponse crearEntrenador(EntrenadorRequest request) {
        log.info("Creando nuevo entrenador con RUT: {}", request.getRut());

        if (entrenadorRepository.existsByRut(request.getRut())) {
            log.warn("RUT duplicado para entrenador: {}", request.getRut());
            throw new RuntimeException("Ya existe un entrenador con ese RUT");
        }

        if (entrenadorRepository.existsByCorreo(request.getCorreo())) {
            log.warn("Correo duplicado para entrenador: {}", request.getCorreo());
            throw new RuntimeException("Ya existe un entrenador con ese correo");
        }

        Entrenador entrenador = new Entrenador();
        entrenador.setRut(request.getRut());
        entrenador.setNombres(request.getNombres());
        entrenador.setApellidos(request.getApellidos());
        entrenador.setEspecialidad(request.getEspecialidad());
        entrenador.setCorreo(request.getCorreo());
        entrenador.setActivo(true);

        Entrenador entrenadorGuardado = entrenadorRepository.save(entrenador);

        log.info("Entrenador creado correctamente con ID: {}", entrenadorGuardado.getIdEntrenador());

        return convertirAResponse(entrenadorGuardado);
    }


    public EntrenadorResponse actualizarEntrenador(Long idEntrenador, EntrenadorRequest request) {
        log.info("Actualizando entrenador con ID: {}", idEntrenador);

        Entrenador entrenadorExistente = entrenadorRepository.findById(idEntrenador)
                .orElseThrow(() -> {
                    log.error("Entrenador no encontrado para actualizar, ID: {}", idEntrenador);
                    return new RuntimeException("Entrenador no encontrado");
                });

        if (!entrenadorExistente.getRut().equals(request.getRut())
                && entrenadorRepository.existsByRut(request.getRut())) {
            log.warn("RUT duplicado al actualizar entrenador: {}", request.getRut());
            throw new RuntimeException("Ya existe un entrenador con ese RUT");
        }

        if (!entrenadorExistente.getCorreo().equals(request.getCorreo())
                && entrenadorRepository.existsByCorreo(request.getCorreo())) {
            log.warn("Correo duplicado al actualizar entrenador: {}", request.getCorreo());
            throw new RuntimeException("Ya existe un entrenador con ese correo");
        }

        entrenadorExistente.setRut(request.getRut());
        entrenadorExistente.setNombres(request.getNombres());
        entrenadorExistente.setApellidos(request.getApellidos());
        entrenadorExistente.setEspecialidad(request.getEspecialidad());
        entrenadorExistente.setCorreo(request.getCorreo());

        Entrenador entrenadorActualizado = entrenadorRepository.save(entrenadorExistente);

        log.info("Entrenador actualizado correctamente con ID: {}", entrenadorActualizado.getIdEntrenador());

        return convertirAResponse(entrenadorActualizado);
    }


    public void eliminarEntrenador(Long idEntrenador) {
        log.info("Eliminando entrenador con ID: {}", idEntrenador);

        if (!entrenadorRepository.existsById(idEntrenador)) {
            log.error("No se puede eliminar: entrenador no existe, ID: {}", idEntrenador);
            throw new RuntimeException("No se puede eliminar: entrenador no existe");
        }

        entrenadorRepository.deleteById(idEntrenador);

        log.info("Entrenador eliminado correctamente con ID: {}", idEntrenador);
    }


    private EntrenadorResponse convertirAResponse(Entrenador entrenador) {
        return new EntrenadorResponse(
                entrenador.getIdEntrenador(),
                entrenador.getRut(),
                entrenador.getNombres(),
                entrenador.getApellidos(),
                entrenador.getEspecialidad(),
                entrenador.getCorreo(),
                entrenador.getActivo()
        );
    }
}