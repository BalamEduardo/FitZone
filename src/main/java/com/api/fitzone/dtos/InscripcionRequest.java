
package com.api.fitzone.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InscripcionRequest(
    @NotNull @Positive Long idSocio,
    @NotNull @Positive Long idClase
) {}
