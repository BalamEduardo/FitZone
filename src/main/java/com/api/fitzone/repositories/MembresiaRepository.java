
package com.api.fitzone.repositories;

import com.api.fitzone.models.membresia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MembresiaRepository extends JpaRepository<membresia, Long> {

    Optional<membresia> findBySocio_IdSocio(Long idSocio);

}
