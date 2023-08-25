package org.example.repository;

import org.example.model.AnalyzedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<AnalyzedData, Long> {
    @Query(nativeQuery = true, value =  
            "WITH filtred_data AS (  " +
            "    SELECT *, LEAD(time) OVER(ORDER BY time) as next_time " +
            "       FROM raw_data_light " +
            "    where hash =  :hash or prev_hash =  :hash  " +
            "  ),  " +
            "   breakpoints AS ( " +
            "    SELECT hash, time as prev_time_rl  " +
            "    FROM filtred_data  " +
            "    WHERE (hash != prev_hash OR prev_hash is null) and hash = :hash " +
            "  )  " +
            "  select fd.time, fd.hash,  " +
            "       CAST(EXTRACT(EPOCH FROM(next_time - prev_time_rl)) * 1000 AS BIGINT) as expected_duration,  " +
            "       fd.is_found, fd.lag  " +
            "  from filtred_data fd  " +
            "  join breakpoints br on br.hash = fd.hash and br.prev_time_rl = ANY (SELECT MAX(prev_time_rl) FROM breakpoints where prev_time_rl <= fd.time)  " +
            "  where fd.hash = :hash  " +
            "  order by time")
    List<AnalyzedData> findAllByHash(String hash);
    @Query(nativeQuery = true, value =
            "WITH filtred_data AS (   " +
            "  select * from ( " +
            "    SELECT *, LEAD(time) OVER(ORDER BY time) as next_time   " +
            "      FROM raw_data_light   " +
            "     where hash = :hash or prev_hash = :hash " +
            "    order by time desc " +
            "  ) tb " +
            "  where hash = :hash " +
            "  limit :amount " +
            " ),  " +
            "  breakpoints AS (   " +
            "   SELECT hash, time as prev_time_rl   " +
            "   FROM filtred_data   " +
            "   WHERE (hash != prev_hash OR prev_hash is null) and hash = :hash " +
            " )   " +
            " select fd.time, fd.hash,   " +
            "    CAST(EXTRACT(EPOCH FROM(next_time - prev_time_rl)) * 1000 AS BIGINT) as expected_duration,   " +
            "    fd.is_found, fd.lag   " +
            " from filtred_data fd   " +
            " join breakpoints br on br.hash = fd.hash and br.prev_time_rl = ANY (SELECT MAX(prev_time_rl) FROM breakpoints where prev_time_rl <= fd.time)   " +
            " where fd.hash = :hash   " +
            " order by time ")
    List<AnalyzedData> findLastByHash(String hash, int amount);

    @Query(nativeQuery = true, value =
            // не идеальный вариант,
            // так как имеет косяк в виде потенциально
            // пропуска первых записей, если они были
            //  частью длинной последовательности
            // записей с одним хешем
            " WITH last_raw_data AS (  " +
            "  select * from   " +
            "       (select * from raw_data_light order by time desc limit :amount) as tb  " +
            "    order by time  " +
            "),  " +
            "lagged_data AS (  " +
            "   SELECT *,  " +
            "     LEAD(time) OVER(ORDER BY time) as next_time  " +
            "   FROM last_raw_data  " +
            "),  " +
            "breakpoints AS (  " +
            "  SELECT hash, time as prev_time_rl  " +
            "    FROM last_raw_data  " +
            "  WHERE hash != prev_hash OR prev_hash is null  " +
            ")  " +
            "select ld.time, ld.hash,  " +
            "        CAST(EXTRACT(EPOCH FROM(next_time - prev_time_rl)) * 1000 AS BIGINT) as expected_duration, " +
            "       ld.is_found, ld.lag  " +
            "from lagged_data ld  " +
            "join breakpoints br on br.hash = ld.hash and br.prev_time_rl = ANY (SELECT MAX(prev_time_rl) FROM breakpoints where prev_time_rl <= ld.time)  " +
            "order by time  ")
    List<AnalyzedData> findLast(int amount);
    @Query(nativeQuery = true, value =
            "WITH lagged_data AS ( " +
            "   SELECT *, " +
            "       LEAD(time) OVER(ORDER BY time) as next_time " +
            "   FROM raw_data_light " +
            "),  " +
            " breakpoints AS ( " +
            "  SELECT hash, time as prev_time_rl " +
            "        FROM raw_data_light " +
            "    WHERE hash != prev_hash OR prev_hash is null " +
            ") " +
            "select ld.time, ld.hash, " +
            "       CAST(EXTRACT(EPOCH FROM(next_time - prev_time_rl)) * 1000 AS BIGINT) as expected_duration, " +
            "       ld.is_found, ld.lag " +
            "from lagged_data ld " +
            "join breakpoints br on br.hash = ld.hash and br.prev_time_rl = ANY (SELECT MAX(prev_time_rl) FROM breakpoints where prev_time_rl <= ld.time) " +
            "order by time")
    List<AnalyzedData> findAllRaws();
}
