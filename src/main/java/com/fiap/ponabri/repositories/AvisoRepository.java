package com.fiap.ponabri.repositories;

import com.fiap.ponabri.entities.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AvisoRepository extends JpaRepository<Aviso, UUID> {
}
