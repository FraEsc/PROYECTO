package com.gymfit.socios.Service;

import com.gymfit.socios.dto.MembresiaRequest;
import com.gymfit.socios.dto.MembresiaResponse;
import com.gymfit.socios.Model.Membresia;
import com.gymfit.socios.Model.Plan;
import com.gymfit.socios.Model.Socio;
import com.gymfit.socios.Repository.MembresiaRepository;
import com.gymfit.socios.Repository.PlanRepository;
import com.gymfit.socios.Repository.SocioRepository;
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
public class MembresiaService {

    private static final Logger log = LoggerFactory.getLogger(MembresiaService.class);

    private final MembresiaRepository membresiaRepository;
    private final SocioRepository socioRepository;
    private final PlanRepository planRepository;

    @Transactional(readOnly = true)
    public List<MembresiaResponse> listarMembresias() {
        log.info("Listando todas las membresías");

        List<Membresia> membresias = membresiaRepository.findAll();
        List<MembresiaResponse> respuesta = new ArrayList<>();

        for (Membresia membresia : membresias) {
            respuesta.add(convertirAResponse(membresia));
        }

        log.info("Se encontraron {} membresías", respuesta.size());
        return respuesta;
    }

    @Transactional(readOnly = true)
    public MembresiaResponse obtenerMembresiaPorId(Long idMembresia) {
        log.info("Buscando membresía con ID: {}", idMembresia);

        Membresia membresia = membresiaRepository.findById(idMembresia)
                .orElseThrow(() -> {
                    log.error("Membresía no encontrada con ID: {}", idMembresia);
                    return new RuntimeException("Membresía no encontrada");
                });

        return convertirAResponse(membresia);
    }


    @Transactional(readOnly = true)
    public List<MembresiaResponse> listarMembresiasPorSocio(Long idSocio) {
        log.info("Listando membresías del socio ID: {}", idSocio);

        if (!socioRepository.existsById(idSocio)) {
            log.error("Socio no encontrado con ID: {}", idSocio);
            throw new RuntimeException("Socio no encontrado");
        }

        List<Membresia> membresias = membresiaRepository.findBySocio_IdSocio(idSocio);
        List<MembresiaResponse> respuesta = new ArrayList<>();

        for (Membresia membresia : membresias) {
            respuesta.add(convertirAResponse(membresia));
        }

        log.info("Se encontraron {} membresías para el socio ID: {}", respuesta.size(), idSocio);
        return respuesta;
    }


    public MembresiaResponse crearMembresia(MembresiaRequest request) {
        log.info("Creando membresía para socio ID: {}", request.getIdSocio());

        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            log.warn("Fecha de fin inválida. Inicio: {}, Fin: {}",
                    request.getFechaInicio(), request.getFechaFin());
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        Socio socio = socioRepository.findById(request.getIdSocio())
                .orElseThrow(() -> {
                    log.error("Socio no encontrado con ID: {}", request.getIdSocio());
                    return new RuntimeException("Socio no encontrado");
                });

        Plan plan = planRepository.findById(request.getIdPlan())
                .orElseThrow(() -> {
                    log.error("Plan no encontrado con ID: {}", request.getIdPlan());
                    return new RuntimeException("Plan no encontrado");
                });

        Membresia membresia = new Membresia();
        membresia.setFechaInicio(request.getFechaInicio());
        membresia.setFechaFin(request.getFechaFin());
        membresia.setEstado(request.getEstado());
        membresia.setSocio(socio);
        membresia.setPlan(plan);

        Membresia membresiaGuardada = membresiaRepository.save(membresia);

        log.info("Membresía creada correctamente con ID: {}", membresiaGuardada.getIdMembresia());

        return convertirAResponse(membresiaGuardada);
    }


    public MembresiaResponse actualizarMembresia(Long idMembresia, MembresiaRequest request) {
        log.info("Actualizando membresía con ID: {}", idMembresia);

        Membresia membresiaExistente = membresiaRepository.findById(idMembresia)
                .orElseThrow(() -> {
                    log.error("Membresía no encontrada para actualizar, ID: {}", idMembresia);
                    return new RuntimeException("Membresía no encontrada");
                });

        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            log.warn("Fecha de fin inválida en actualización. Inicio: {}, Fin: {}",
                    request.getFechaInicio(), request.getFechaFin());
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        Socio socio = socioRepository.findById(request.getIdSocio())
                .orElseThrow(() -> {
                    log.error("Socio no encontrado con ID: {}", request.getIdSocio());
                    return new RuntimeException("Socio no encontrado");
                });

        Plan plan = planRepository.findById(request.getIdPlan())
                .orElseThrow(() -> {
                    log.error("Plan no encontrado con ID: {}", request.getIdPlan());
                    return new RuntimeException("Plan no encontrado");
                });

        membresiaExistente.setFechaInicio(request.getFechaInicio());
        membresiaExistente.setFechaFin(request.getFechaFin());
        membresiaExistente.setEstado(request.getEstado());
        membresiaExistente.setSocio(socio);
        membresiaExistente.setPlan(plan);

        Membresia membresiaActualizada = membresiaRepository.save(membresiaExistente);

        log.info("Membresía actualizada correctamente con ID: {}", membresiaActualizada.getIdMembresia());

        return convertirAResponse(membresiaActualizada);
    }

    public void eliminarMembresia(Long idMembresia) {
        log.info("Eliminando membresía con ID: {}", idMembresia);

        if (!membresiaRepository.existsById(idMembresia)) {
            log.error("No se puede eliminar: membresía no existe, ID: {}", idMembresia);
            throw new RuntimeException("No se puede eliminar: membresía no existe");
        }

        membresiaRepository.deleteById(idMembresia);

        log.info("Membresía eliminada correctamente con ID: {}", idMembresia);
    }


    @Transactional(readOnly = true)
    public boolean verificarMembresiaActiva(Long idSocio) {
        log.info("Verificando membresía activa para socio ID: {}", idSocio);

        boolean tieneActiva = membresiaRepository.existsBySocio_IdSocioAndEstado(idSocio, "ACTIVA");

        log.info("Socio ID: {} tiene membresía activa: {}", idSocio, tieneActiva);

        return tieneActiva;
    }


    private MembresiaResponse convertirAResponse(Membresia membresia) {
        return new MembresiaResponse(
                membresia.getIdMembresia(),
                membresia.getFechaInicio(),
                membresia.getFechaFin(),
                membresia.getEstado(),
                membresia.getSocio().getIdSocio(),
                membresia.getSocio().getNombres() + " " + membresia.getSocio().getApellidos(),
                membresia.getPlan().getIdPlan(),
                membresia.getPlan().getNombre()
        );
    }
}