package org.example.repository;

import org.example.model.AnalyzedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<AnalyzedData, Long> {
    @Query(nativeQuery = true, value =  
            "WITH filtred_data AS (   " +
            "  select * from (   " +
            "   SELECT *,   " +
            "    LAG(hash) OVER(ORDER BY time) as prev_hash " +
            "   FROM raw_data_light) t   " +
            "  where hash = :hash or prev_hash = :hash   " +
            "),   " +
            " breakpoints AS (   " +
            "  SELECT hash, time, LEAD(time) OVER(ORDER BY time) as next_time, is_found, lag " +
            "  FROM filtred_data   " +
            "  WHERE hash != prev_hash OR prev_hash is null   " +
            ")   " +
            "select br.time, br.hash,   " +
            "     CAST(EXTRACT(EPOCH FROM(next_time - time)) * 1000 AS BIGINT) as expected_duration,   " +
            "     br.is_found, br.lag   " +
            "from breakpoints br " +
            "where br.hash = :hash " +
            "order by time")
    List<AnalyzedData> findAllByHash(String hash);
    @Query(nativeQuery = true, value =
            "WITH filtred_data AS (   " +
            "  select * from (   " +
            "   SELECT *,   " +
            "    LAG(hash) OVER(ORDER BY time) as prev_hash " +
            "   FROM raw_data_light" +
            "   LIMIT :amount) t   " +
            "  where hash = :hash or prev_hash = :hash   " +
            "),   " +
            " breakpoints AS (   " +
            "  SELECT hash, time, LEAD(time) OVER(ORDER BY time) as next_time, is_found, lag " +
            "  FROM filtred_data   " +
            "  WHERE hash != prev_hash OR prev_hash is null   " +
            ")   " +
            "select br.time, br.hash,   " +
            "     CAST(EXTRACT(EPOCH FROM(next_time - time)) * 1000 AS BIGINT) as expected_duration,   " +
            "     br.is_found, br.lag   " +
            "from breakpoints br " +
            "where br.hash = :hash " +
            "order by time")
    List<AnalyzedData> findLastByHash(String hash, int amount);

    @Query(nativeQuery = true, value =
            // не идеальный вариант,
            // так как имеет косяк в виде потенциально
            // некоректного обсчета первых записей
            // если первые записи были частью длинной последовательности
            // записей с одним хешем
            " WITH  last_raw_data AS ( " +
            "  select * from  " +
            "       (select * from raw_data_light order by time desc limit :amount) as tb " +
            "    order by time " +
            "), " +
            "lagged_data AS ( " +
            "   SELECT *, " +
            "     LEAD(time) OVER(ORDER BY time) as next_time " +
            "   FROM last_raw_data " +
            "), " +
            " breakpoints AS ( " +
            "  SELECT hash, time as prev_time_rl " +
            "    FROM ( " +
            "        SELECT *, " +
            "           LAG(hash) OVER(ORDER BY time) as prev_hash " +
            "        FROM last_raw_data " +
            "    ) sub " +
            "    WHERE hash != prev_hash OR prev_hash is null " +
            ") " +
            "select ld.time, ld.hash, " +
            "       CAST(EXTRACT(EPOCH FROM(next_time - prev_time_rl)) * 1000 AS BIGINT) as expected_duration, " +
            "       ld.is_found, ld.lag " +
            "from lagged_data ld " +
            "join breakpoints br on br.hash = ld.hash and br.prev_time_rl = ANY (SELECT MAX(prev_time_rl) FROM breakpoints where prev_time_rl <= ld.time) " +
            "order by time")
    List<AnalyzedData> findLast(int amount);
    @Query(nativeQuery = true, value =
            "WITH lagged_data AS ( " +
            "       SELECT *, " +
            "           LEAD(time) OVER(ORDER BY time) as next_time " +
            "       FROM raw_data_light " +
            "    ), " +
            "     breakpoints AS ( " +
            "        SELECT hash, time as prev_time_rl " +
            "            FROM ( " +
            "                  SELECT *, " +
            "                       LAG(hash) OVER(ORDER BY time) as prev_hash " +
            "                  FROM raw_data_light " +
            "            ) sub " +
            "            WHERE hash != prev_hash OR prev_hash is null " +
            "    ) " +
            "    select ld.time, ld.hash, " +
            "               CAST(EXTRACT(EPOCH FROM(next_time - prev_time_rl)) * 1000 AS BIGINT) as expected_duration, " +
            "               ld.is_found, ld.lag " +
            "    from lagged_data ld " +
            "    join breakpoints br on br.hash = ld.hash and br.prev_time_rl = ANY (SELECT MAX(prev_time_rl) FROM breakpoints where prev_time_rl <= ld.time) " +
            "    order by time")
    List<AnalyzedData> findAllRaws();
}
