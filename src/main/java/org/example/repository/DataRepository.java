package org.example.repository;

import org.example.model.AnalyzedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<AnalyzedData, Long> {
    @Query(nativeQuery = true, value =  
            "WITH filtred_data AS ( " +
            "  select *, LEAD(time) OVER(ORDER BY time) as next_time from ( " +
            "    SELECT *, " +
            "     LAG(hash) OVER(ORDER BY time) as prev_hash," +
            "     LEAD(hash) OVER(ORDER BY time) as next_hash " +
            "    FROM raw_data_light) t    " +
            "  where (hash = :hash AND next_hash != :hash) OR (prev_hash = :hash AND hash != :hash) " +
            ")" +
            "select fd.time, fd.hash, fd.is_Found, fd.lag," +
            "   CAST(EXTRACT(EPOCH FROM(next_time - prev_relational_time)) * 1000 AS BIGINT) as expected_duration " +
            "from filtred_data fd  " +
            "where fd.hash = :hash  " +
            "order by time")
    List<AnalyzedData> findAllByHash(String hash);
    @Query(nativeQuery = true, value =
            "WITH filtred_data AS ( " +
            "  select *, LEAD(time) OVER(ORDER BY time) as next_time from ( " +
            "    SELECT *, " +
            "     LAG(hash) OVER(ORDER BY time) as prev_hash," +
            "     LEAD(hash) OVER(ORDER BY time) as next_hash " +
            "    FROM raw_data_light) t " +
            "  where (hash = :hash AND next_hash != :hash) OR (prev_hash = :hash AND hash != :hash) " +
            "  ORDER BY time DESC " +
            "  LIMIT :amount * 2" +
            ")" +
            "select fd.time, fd.hash, fd.is_Found, fd.lag," +
            "   CAST(EXTRACT(EPOCH FROM(next_time - prev_relational_time)) * 1000 AS BIGINT) as expected_duration " +
            "from filtred_data fd  " +
            "where fd.hash = :hash  " +
            "order by time")
    List<AnalyzedData> findLastByHash(String hash, int amount);

    @Query(nativeQuery = true, value =
            "WITH lagged_data AS ( " +
            "   SELECT * from (" +
            "       SELECT *, " +
            "           LEAD(time) OVER(ORDER BY time) as next_time " +
            "       FROM raw_data_light " +
            "       ORDER BY time desc " +
            "       LIMIT :amount " +
            "    ) as t" +
            "    ORDER BY time" +
            "    ) " +
            "    select ld.time, ld.hash, " +
            "               CAST(EXTRACT(EPOCH FROM(next_time - prev_relational_time)) * 1000 AS BIGINT) as expected_duration, " +
            "               ld.is_found, ld.lag " +
            "    from lagged_data ld " +
            "    order by time")
    List<AnalyzedData> findLast(int amount);
    @Query(nativeQuery = true, value =
            "WITH lagged_data AS ( " +
            "       SELECT *, " +
            "           LEAD(time) OVER(ORDER BY time) as next_time " +
            "       FROM raw_data_light " +
            "    ) " +
            "    select ld.time, ld.hash, " +
            "               CAST(EXTRACT(EPOCH FROM(next_time - prev_relational_time)) * 1000 AS BIGINT) as expected_duration, " +
            "               ld.is_found, ld.lag " +
            "    from lagged_data ld " +
            "    order by time")
    List<AnalyzedData> findAllRaws();
}
