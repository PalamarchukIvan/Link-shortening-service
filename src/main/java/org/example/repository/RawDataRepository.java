package org.example.repository;

import org.example.model.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "insert into raw_data(time, hash, lag, is_found) values (CURRENT_TIMESTAMP at time zone 'UTC', :hash, :duration, :isFound )")
    void saveData(String hash, Long duration, boolean isFound);
    List<RawData> findAllByHash(String hash);
}
