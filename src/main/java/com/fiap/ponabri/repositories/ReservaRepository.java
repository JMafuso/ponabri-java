package com.fiap.ponabri.repositories;

import com.fiap.ponabri.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findByCodigoReserva(String codigoReserva);
}
