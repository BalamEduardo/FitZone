
package com.api.fitzone.controllers;

import com.api.fitzone.dtos.CupoResponse;
import com.api.fitzone.services.ClaseService;

import org.springframework.web.bind.annotation.*;
import com.api.fitzone.dtos.ClaseResponse;
import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping("/{idClase}/cupo")
    public CupoResponse consultarCupo(
            @PathVariable Long idClase) {

        return claseService.consultarCupo(idClase);
    }

    @GetMapping
    public List<ClaseResponse> consultarClases() {
        return claseService.consultarClases();
    }

}
