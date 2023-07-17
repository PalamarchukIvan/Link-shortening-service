package org.example.repository;

import org.example.model.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.Instant;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, Long> {
    @Query(nativeQuery = true, value = "insert into raw_data(id, time, hash) values (NEXTVAL('raw_data_seq'), CURRENT_TIMESTAMP, :hash)")
    void saveData(String hash) throws SQLException;
}
