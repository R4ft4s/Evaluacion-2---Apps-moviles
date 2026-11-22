package com.opycorp.backend.repository;

import com.opycorp.backend.model.EventoExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoExternoRepository extends JpaRepository<EventoExterno, Long> {
}
