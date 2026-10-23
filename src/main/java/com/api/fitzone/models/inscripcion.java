
package com.api.fitzone.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "inscripciones",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_socio_clase",
        columnNames = {"socio_id", "clase_id"}
    )
)
public class inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInscripcion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "socio_id", nullable = false)
    private socio socio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clase_id", nullable = false)
    private clase clase;

    @Column(nullable = false)
    private LocalDateTime fechaInscripcion =
            LocalDateTime.now();

    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public socio getSocio() {
        return socio;
    }

    public void setSocio(socio socio) {
        this.socio = socio;
    }

    public clase getClase() {
        return clase;
    }

    public void setClase(clase clase) {
        this.clase = clase;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

}
