
package com.api.fitzone.dtos;

public record CupoResponse(
    Long idClase,
    Integer capacidadMaxima,
    Long inscritos,
    Long lugaresDisponibles,
    Boolean hayCupo
) {}
