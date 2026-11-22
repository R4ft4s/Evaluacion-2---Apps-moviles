package com.opycorp.backend.repository;

import com.opycorp.backend.model.Oportunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OportunidadRepository extends JpaRepository<Oportunidad, Long> {
}
