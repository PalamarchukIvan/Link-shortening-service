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
    @Query(nativeQuery = true, value =  "select * from raw_data rd where rd.hash = :hash")
    List<RawData> findAllByHash(String hash);
    @Query(nativeQuery = true, value =  " select * from " +
                                        "   (select * from raw_data where hash = :hash order by time desc limit :amount) as tb" +
                                        " order by time")
    List<RawData> findLastByHash(String hash, int amount);

    @Query(nativeQuery = true, value =  " select * from " +
                                        "   (select * from raw_data order by time desc limit :amount) as tb" +
                                        " order by time")
    List<RawData> findLast(int amount);
}
