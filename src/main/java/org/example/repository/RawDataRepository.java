package org.example.repository;

import org.example.model.RawDataLight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface RawDataRepository extends JpaRepository<RawDataLight, Long> {
    @Query(nativeQuery = true, value = "select * from raw_data_light order by time desc limit 1")
    RawDataLight findLast();
    @Query(nativeQuery = true, value = "select * from raw_data_light where time = :time")
    RawDataLight findById(Instant time);
}
