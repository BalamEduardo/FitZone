
package com.api.fitzone.controllers;

import com.api.fitzone.dtos.ErrorResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> manejarError(
            ResponseStatusException ex) {

        String codigo = ex.getReason() == null
                ? "ERROR_DESCONOCIDO"
                : ex.getReason();

        String mensaje = switch (codigo) {
            case "INSCRIPCION_DUPLICADA" ->
                "El socio ya esta inscrito en esta clase.";

            case "CUPO_AGOTADO" ->
                "La clase ya no tiene lugares disponibles.";

            case "MEMBRESIA_VENCIDA" ->
                "La membresia del socio esta vencida.";

            case "MEMBRESIA_NO_ENCONTRADA" ->
                "El socio no tiene una membresia registrada.";

            case "SOCIO_NO_ENCONTRADO" ->
                "El socio solicitado no existe.";

            case "CLASE_NO_ENCONTRADA" ->
                "La clase solicitada no existe.";

            case "ID_INVALIDO" ->
                "El identificador proporcionado no es valido.";
                
            case "CLASE_YA_INICIADA" ->
                "No es posible inscribirse a una clase que ya comenzo.";

            default -> "No se pudo completar la operacion.";
        };

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse(codigo, mensaje));
    }
}
