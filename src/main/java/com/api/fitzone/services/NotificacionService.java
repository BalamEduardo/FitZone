
package com.api.fitzone.services;

import com.api.fitzone.dtos.InscripcionResponse;
import com.api.fitzone.models.inscripcion;
import com.api.fitzone.repositories.InscripcionRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacionService {

    private final InscripcionRepository inscripcionRepository;

    public NotificacionService(
            InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    // Confirmacion inmediata
    public void enviarConfirmacion(
            InscripcionResponse respuesta) {

        System.out.println("\n[CONFIRMACION FITZONE]");
        System.out.println(
            "Socio ID: " + respuesta.idSocio()
        );
        System.out.println(
            "Clase ID: " + respuesta.idClase()
        );
        System.out.println(
            "Inscripcion registrada correctamente.\n"
        );
    }

    // Revision automatica cada 10 segundos
    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void enviarRecordatorios() {

        LocalDateTime ahora = LocalDateTime.now();

        List<inscripcion> inscripciones =
                inscripcionRepository.findAll();

        for (inscripcion inscripcion : inscripciones) {

            if (Boolean.TRUE.equals(
                    inscripcion.getRecordatorioEnviado())) {
                continue;
            }

            LocalDateTime inicio =
                inscripcion.getClase().getFechaHora();

            LocalDateTime momentoRecordatorio =
                inicio.minusHours(2);

            if (!ahora.isBefore(momentoRecordatorio)
                    && ahora.isBefore(inicio)) {

                System.out.println(
                    "\n[RECORDATORIO FITZONE]"
                );
                System.out.println(
                    "Socio: " +
                    inscripcion.getSocio().getNombre()
                );
                System.out.println(
                    "Clase: " +
                    inscripcion.getClase().getNombre()
                );
                System.out.println(
                    "Horario: " + inicio
                );
                System.out.println(
                    "Tu clase comienza pronto.\n"
                );

                inscripcion.setRecordatorioEnviado(true);
            }
        }
    }
}
