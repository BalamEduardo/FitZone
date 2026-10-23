
package com.api.fitzone.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "membresias")
public class membresia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMembresia;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @OneToOne(optional = false) @JoinColumn(name = "socio_id", nullable = false, unique = true)
    private socio socio;

    public Long getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(Long idMembresia) {
        this.idMembresia = idMembresia;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public socio getSocio() {
        return socio;
    }

    public void setSocio(socio socio) {
        this.socio = socio;
    }

}
