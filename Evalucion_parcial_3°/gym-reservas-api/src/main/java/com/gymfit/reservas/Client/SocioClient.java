package com.gymfit.reservas.Client;

import com.gymfit.reservas.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
        name = "gym-socios-api",
        url = "${gym.socios.url}"
)
public interface SocioClient {
    @GetMapping("/api/v1/membresias/verificar/{idSocio}")
    ApiResponse<Boolean> verificarMembresiaActiva(@PathVariable Long idSocio);
}