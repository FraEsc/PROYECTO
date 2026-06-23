package com.gymfit.socios.Service;

import com.gymfit.socios.dto.SocioRequest;
import com.gymfit.socios.dto.SocioResponse;
import com.gymfit.socios.Model.Socio;
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
public class SocioService {

    private static final Logger log = LoggerFactory.getLogger(SocioService.class);

    private final SocioRepository socioRepository;

    @Transactional(readOnly = true)
    public List<SocioResponse> listarSocios() {
        log.info("Listando todos los socios");

        List<Socio> socios = socioRepository.findAll();
        List<SocioResponse> respuesta = new ArrayList<>();

        for (Socio socio : socios) {
            respuesta.add(convertirAResponse(socio));
        }

        log.info("Se encontraron {} socios", respuesta.size());
        return respuesta;
    }

    @Transactional(readOnly = true)
    public SocioResponse obtenerSocioPorId(Long idSocio) {
        log.info("Buscando socio con ID: {}", idSocio);

        Socio socio = socioRepository.findById(idSocio)
                .orElseThrow(() -> {
                    log.error("Socio no encontrado con ID: {}", idSocio);
                    return new RuntimeException("Socio no encontrado");
                });

        return convertirAResponse(socio);
    }


    public SocioResponse registrarSocio(SocioRequest request) {
        log.info("Registrando nuevo socio con RUT: {}", request.getRut());

        if (socioRepository.existsByRut(request.getRut())) {
            log.warn("RUT duplicado: {}", request.getRut());
            throw new RuntimeException("Ya existe un socio con ese RUT");
        }

        if (socioRepository.existsByCorreo(request.getCorreo())) {
            log.warn("Correo duplicado: {}", request.getCorreo());
            throw new RuntimeException("Ya existe un socio con ese correo");
        }

        Socio socio = new Socio();
        socio.setRut(request.getRut());
        socio.setNombres(request.getNombres());
        socio.setApellidos(request.getApellidos());
        socio.setCorreo(request.getCorreo());
        socio.setTelefono(request.getTelefono());
        socio.setActivo(true);

        Socio socioGuardado = socioRepository.save(socio);

        log.info("Socio registrado correctamente con ID: {}", socioGuardado.getIdSocio());

        return convertirAResponse(socioGuardado);
    }

    public SocioResponse actualizarSocio(Long idSocio, SocioRequest request) {
        log.info("Actualizando socio con ID: {}", idSocio);

        Socio socioExistente = socioRepository.findById(idSocio)
                .orElseThrow(() -> {
                    log.error("Socio no encontrado para actualizar, ID: {}", idSocio);
                    return new RuntimeException("Socio no encontrado");
                });

        if (!socioExistente.getRut().equals(request.getRut())
                && socioRepository.existsByRut(request.getRut())) {
            log.warn("RUT duplicado al actualizar: {}", request.getRut());
            throw new RuntimeException("Ya existe un socio con ese RUT");
        }

        if (!socioExistente.getCorreo().equals(request.getCorreo())
                && socioRepository.existsByCorreo(request.getCorreo())) {
            log.warn("Correo duplicado al actualizar: {}", request.getCorreo());
            throw new RuntimeException("Ya existe un socio con ese correo");
        }

        socioExistente.setRut(request.getRut());
        socioExistente.setNombres(request.getNombres());
        socioExistente.setApellidos(request.getApellidos());
        socioExistente.setCorreo(request.getCorreo());
        socioExistente.setTelefono(request.getTelefono());

        Socio socioActualizado = socioRepository.save(socioExistente);

        log.info("Socio actualizado correctamente con ID: {}", socioActualizado.getIdSocio());

        return convertirAResponse(socioActualizado);
    }

    public void eliminarSocio(Long idSocio) {
        log.info("Eliminando socio con ID: {}", idSocio);

        if (!socioRepository.existsById(idSocio)) {
            log.error("No se puede eliminar: socio no existe, ID: {}", idSocio);
            throw new RuntimeException("No se puede eliminar: socio no existe");
        }

        socioRepository.deleteById(idSocio);

        log.info("Socio eliminado correctamente con ID: {}", idSocio);
    }


    private SocioResponse convertirAResponse(Socio socio) {
        return new SocioResponse(
                socio.getIdSocio(),
                socio.getRut(),
                socio.getNombres(),
                socio.getApellidos(),
                socio.getCorreo(),
                socio.getTelefono()
        );
    }
}