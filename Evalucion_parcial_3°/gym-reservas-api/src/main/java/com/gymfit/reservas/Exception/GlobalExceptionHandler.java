package com.gymfit.reservas.Exception;

import com.gymfit.reservas.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> manejarValidaciones(
            MethodArgumentNotValidException ex
    ) {
        String mensajeError = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        log.warn("Error de validación: {}", mensajeError);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        mensajeError,
                        true,
                        null
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> manejarRuntimeException(
            RuntimeException ex
    ) {
        log.error("Error de negocio: {}", ex.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ex.getMessage() != null &&
                (ex.getMessage().toLowerCase().contains("no encontrado")
                        || ex.getMessage().toLowerCase().contains("no existe"))) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status)
                .body(new ApiResponse<>(
                        status.value(),
                        ex.getMessage(),
                        true,
                        null
                ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> manejarException(Exception ex) {
        log.error("Error interno del servidor: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error interno del servidor",
                        true,
                        null
                ));
    }
}