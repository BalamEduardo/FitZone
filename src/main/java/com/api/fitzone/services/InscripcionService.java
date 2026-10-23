
package com.api.fitzone.services;

import com.api.fitzone.models.*;
import com.api.fitzone.dtos.InscripcionResponse;
import com.api.fitzone.repositories.*;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final ClaseRepository claseRepository;
    private final SocioRepository socioRepository;
    private final MembresiaService membresiaService;

    public InscripcionService(
            InscripcionRepository inscripcionRepository,
            ClaseRepository claseRepository,
            SocioRepository socioRepository,
            MembresiaService membresiaService) {

        this.inscripcionRepository = inscripcionRepository;
        this.claseRepository = claseRepository;
        this.socioRepository = socioRepository;
        this.membresiaService = membresiaService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public InscripcionResponse inscribirEnClase(
            Long idSocio, Long idClase) {

        // 1. Validar identificadores
        if (idSocio == null || idSocio <= 0 ||
            idClase == null || idClase <= 0) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "ID_INVALIDO"
            );
        }

        // 2. Validar membresia
        membresiaService.validarMembresia(idSocio);

        // 3. Buscar y bloquear la clase
        clase clase = claseRepository.findByIdClase(idClase)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "CLASE_NO_ENCONTRADA"
            ));

        // 4. Evitar inscripciones duplicadas
        boolean yaInscrito = inscripcionRepository
            .existsBySocio_IdSocioAndClase_IdClase(
                idSocio, idClase
            );

        if (yaInscrito) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "INSCRIPCION_DUPLICADA"
            );
        }

        // 5. Verificar cupo
        long inscritos = inscripcionRepository
            .countByClase_IdClase(idClase);

        if (inscritos >= clase.getCapacidadMaxima()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "CUPO_AGOTADO"
            );
        }

        // 6. Obtener socio
        socio socio = socioRepository.findById(idSocio)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "SOCIO_NO_ENCONTRADO"
            ));

        // 7. Crear inscripcion
        inscripcion inscripcion = new inscripcion();
        inscripcion.setSocio(socio);
        inscripcion.setClase(clase);
            
        // 8. Guardar en MySQL
        inscripcion guardada =
            inscripcionRepository.save(inscripcion);

        // 9. Devolver respuesta
        return new InscripcionResponse(
            guardada.getIdInscripcion(),
            idSocio,
            idClase,
            guardada.getFechaInscripcion(),
            "Inscripcion registrada correctamente"
        );
    }
}
