
package com.api.fitzone.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EstadoController {

    @GetMapping("/api/estado")
    public Map<String, String> consultarEstado() {
        return Map.of(
            "aplicacion", "FitZone",
            "estado", "Funcionando"
        );
    }
}
