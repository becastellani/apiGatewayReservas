package com.salaservice.demo.domain.repository;

import com.salaservice.demo.domain.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {}

