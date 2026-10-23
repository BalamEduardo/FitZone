
package com.api.fitzone.dtos;

import java.time.LocalDateTime;

public record InscripcionResponse(
    Long idInscripcion,
    Long idSocio,
    Long idClase,
    LocalDateTime fechaInscripcion,
    String mensaje
) {}
