
package com.api.fitzone.services;

import com.api.fitzone.models.clase;
import com.api.fitzone.dtos.CupoResponse;
import com.api.fitzone.repositories.ClaseRepository;
import com.api.fitzone.repositories.InscripcionRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import com.api.fitzone.dtos.ClaseResponse;
import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final InscripcionRepository inscripcionRepository;

    public ClaseService(
            ClaseRepository claseRepository,
            InscripcionRepository inscripcionRepository) {

        this.claseRepository = claseRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    @Transactional(readOnly = true)
    public CupoResponse consultarCupo(Long idClase) {

        if (idClase == null || idClase <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ID_INVALIDO");
        }

        clase clase = claseRepository.findById(idClase)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "CLASE_NO_ENCONTRADA"));

        long inscritos = inscripcionRepository.countByClase_IdClase(idClase);

        long disponibles = Math.max(
                0L,
                clase.getCapacidadMaxima() - inscritos);

        return new CupoResponse(
                clase.getIdClase(),
                clase.getCapacidadMaxima(),
                inscritos,
                disponibles,
                disponibles > 0);
    }

    @Transactional(readOnly = true)
    public List<ClaseResponse> consultarClases() {

        return claseRepository.findAll()
                .stream()
                .map(clase -> new ClaseResponse(
                        clase.getIdClase(),
                        clase.getNombre(),
                        clase.getSucursal(),
                        clase.getFechaHora(),
                        clase.getCapacidadMaxima()))
                .toList();
    }

}
