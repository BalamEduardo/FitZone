
package com.api.fitzone.services;

import com.api.fitzone.models.membresia;
import com.api.fitzone.repositories.MembresiaRepository;
import com.api.fitzone.repositories.SocioRepository;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MembresiaService {

    private final MembresiaRepository membresiaRepository;
    private final SocioRepository socioRepository;

    public MembresiaService(
            MembresiaRepository membresiaRepository,
            SocioRepository socioRepository) {

        this.membresiaRepository = membresiaRepository;
        this.socioRepository = socioRepository;
    }

    public void validarMembresia(Long idSocio) {

        if (idSocio == null || idSocio <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "ID_INVALIDO"
            );
        }

        if (!socioRepository.existsById(idSocio)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "SOCIO_NO_ENCONTRADO"
            );
        }

        membresia membresia = membresiaRepository.findBySocio_IdSocio(idSocio)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "MEMBRESIA_NO_ENCONTRADA"
            ));

        if (membresia.getFechaVencimiento()
                .isBefore(LocalDate.now())) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "MEMBRESIA_VENCIDA"
            );
        }
    }
}
