
package com.api.fitzone.controllers;

import com.api.fitzone.dtos.InscripcionRequest;
import com.api.fitzone.dtos.InscripcionResponse;
import com.api.fitzone.services.InscripcionService;
import com.api.fitzone.services.NotificacionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final NotificacionService notificacionService;

    public InscripcionController(
            InscripcionService inscripcionService,
            NotificacionService notificacionService) {

        this.inscripcionService = inscripcionService;
        this.notificacionService = notificacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InscripcionResponse inscribir(
            @Valid @RequestBody InscripcionRequest request) {

        InscripcionResponse respuesta =
            inscripcionService.inscribirEnClase(
                request.idSocio(),
                request.idClase()
            );

        notificacionService.enviarConfirmacion(respuesta);

        return respuesta;
    }
}
