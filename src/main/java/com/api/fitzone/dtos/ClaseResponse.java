
package com.api.fitzone.dtos;

import java.time.LocalDateTime;

public record ClaseResponse(
    Long idClase,
    String nombre,
    String sucursal,
    LocalDateTime fechaHora,
    Integer capacidadMaxima
) {}
