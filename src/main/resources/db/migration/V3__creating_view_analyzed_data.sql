create view analyzed_data_view as(
    WITH lagged_data AS (
       SELECT *,
           LEAD(time) OVER(ORDER BY time) as next_time
       FROM raw_data_light
    ),
     breakpoints AS (
        SELECT hash, time as prev_time_rl
            FROM (
                  SELECT *,
                       LAG(hash) OVER(ORDER BY time) as prev_hash
                  FROM raw_data_light
            ) sub
            WHERE hash != prev_hash OR prev_hash is null
    )

    select ld.time, ld.hash,
               CAST(EXTRACT(EPOCH FROM(next_time - prev_time_rl)) * 1000 AS BIGINT) as expected_duration,
               ld.is_found, ld.lag
    from lagged_data ld
    join breakpoints br on br.hash = ld.hash and br.prev_time_rl = ANY (SELECT MAX(prev_time_rl) FROM breakpoints where prev_time_rl <= ld.time)
    order by time
)
