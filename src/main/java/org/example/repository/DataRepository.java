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
            "WITH " +
            "last_data AS(" +
            "    SELECT * from raw_data_light order by time desc limit :amount" +
            "), " +
            " lagged_data AS ( " +
            "       SELECT *, " +
            "           LEAD(time) OVER(ORDER BY time) as next_time " +
            "       FROM last_data " +
            "    ), " +
            "     breakpoints AS ( " +
            "        SELECT hash, time as time_br, " +
            "               LEAD(time) OVER(ORDER BY time) as next_time_ " +
            "            FROM ( " +
            "                  SELECT *, " +
            "                       LAG(hash) OVER(ORDER BY time) as prev_hash" +
            "                  FROM last_data " +
            "            ) sub " +
            "            WHERE hash != prev_hash OR prev_hash is null " +
            "    ) " +
            "    select ld.time, ld.hash, time_br," +
            "               CAST(EXTRACT(EPOCH FROM(next_time - time_br)) * 1000 AS BIGINT) as expected_duration, " +
            "               ld.is_found, ld.lag " +
            "    from lagged_data ld " +
            "    join breakpoints br on br.hash = ld.hash and ((ld.time < br.next_time_ OR br.next_time_ is null) and ld.time >= time_br)" +
            "    order by time ")
    List<AnalyzedData> findLast(int amount);
    @Query(nativeQuery = true, value =
            "WITH lagged_data AS ( " +
            "       SELECT *, " +
            "           LEAD(time) OVER(ORDER BY time) as next_time " +
            "       FROM raw_data_light " +
            "    ), " +
            "     breakpoints AS ( " +
            "        SELECT hash, time as time_br, " +
            "               LEAD(time) OVER(ORDER BY time) as next_time_ " +
            "            FROM ( " +
            "                  SELECT *, " +
            "                       LAG(hash) OVER(ORDER BY time) as prev_hash" +
            "                  FROM raw_data_light " +
            "            ) sub " +
            "            WHERE hash != prev_hash OR prev_hash is null " +
            "    ) " +
            "    select ld.time, ld.hash, time_br," +
            "               CAST(EXTRACT(EPOCH FROM(next_time - time_br)) * 1000 AS BIGINT) as expected_duration, " +
            "               ld.is_found, ld.lag " +
            "    from lagged_data ld " +
            "    join breakpoints br on br.hash = ld.hash and ((ld.time < br.next_time_ OR br.next_time_ is null) and ld.time >= time_br)" +
            "    order by time ")
    List<AnalyzedData> findAllRaws();
}
