package org.example.repository;

import org.example.model.RawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface RawDataRepository extends JpaRepository<RawData, Long> {
    @Query(nativeQuery = true, value = "insert into raw_data(time, hash, lag, is_found) values (CURRENT_TIMESTAMP at time zone 'UTC', :hash, :duration, :isFound )")
    void saveData(String hash, Long duration, boolean isFound) throws SQLException;
    @Query(nativeQuery = true, value =  "SELECT * FROM ( " +
                                        "   SELECT * FROM raw_data\n" +
                                        "       WHERE expected_duration IS NOT NULL OR \n" +
                                        "         expected_duration IS NULL AND \n" +
                                        "            time IN (SELECT MAX(time) FROM raw_data)) as tb\n" +
                                        "where hash = :hash")
    List<RawData> findAllByHash(String hash);
    @Query(nativeQuery = true, value =  " SELECT * FROM (" +
                                        "   SELECT * FROM ( " +
                                        "   SELECT * FROM raw_data\n" +
                                        "       WHERE expected_duration IS NOT NULL OR \n" +
                                        "         expected_duration IS NULL AND \n" +
                                        "            time IN (SELECT MAX(time) FROM raw_data)) as tb\n" +
                                        " where hash = :hash" +
                                        " ORDER BY time DESC LIMIT :amount) as limitedTB" +
                                        " ORDER BY time")
    List<RawData> findLastByHash(String hash, int amount);

    @Query(nativeQuery = true, value =  "SELECT * FROM raw_data\n" +
                                        "   WHERE expected_duration IS NOT NULL OR \n" +
                                        "      expected_duration IS NULL AND \n" +
                                        "          time IN (SELECT MAX(time) FROM raw_data)")
    List<RawData> findAllFiltered();

    @Query(nativeQuery = true, value =  " SELECT * FROM(" +
                                        "  SELECT * FROM(" +
                                        "    SELECT * FROM raw_data\n" +
                                        "        WHERE expected_duration IS NOT NULL OR \n" +
                                        "         expected_duration IS NULL AND \n" +
                                        "               time IN (SELECT MAX(time) FROM raw_data)) as tb" +
                                        " ORDER BY time DESC LIMIT :amount) as limitedTB" +
                                        " ORDER BY time")
    List<RawData> findLast(int amount);
}
