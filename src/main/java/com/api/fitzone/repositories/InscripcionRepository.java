package com.api.fitzone.repositories;

import com.api.fitzone.models.inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<inscripcion, Long> {

    long countByClase_IdClase(Long idClase);

    boolean existsBySocio_IdSocioAndClase_IdClase(Long idSocio, Long idClase);

}
