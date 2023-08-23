package org.example.repository;

import org.example.model.RawDataLight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataRepository extends JpaRepository<RawDataLight, Long> { }
