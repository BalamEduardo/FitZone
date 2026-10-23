
package com.api.fitzone.repositories;

import com.api.fitzone.models.clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface ClaseRepository
        extends JpaRepository<clase, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<clase> findByIdClase(Long idClase);

}
