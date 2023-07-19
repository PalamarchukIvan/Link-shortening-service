CREATE EXTENSION IF NOT EXISTS timescaledb;
--таблица с данными
CREATE SEQUENCE IF NOT EXISTS raw_data_seq;
create table IF NOT EXISTS raw_data (
                          id INT NOT NULL DEFAULT NEXTVAL('raw_data_seq'),
                          time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          hash VARCHAR,
                          expected_duration INTERVAL,
                          lag BIGINT,
                          is_found BOOLEAN,
                          PRIMARY KEY(id, time)
);

select create_hypertable('raw_data', 'time');

-- --Continuous Aggregate MV
-- CREATE MATERIALIZED VIEW analyzed_data with(timescaledb.continuous) as
-- select time_bucket(INTERVAL '1 minute', time) AS time_stamp,
--        hash,
--        SUM(expected_duration) AS expected_duration
-- from raw_data group by time_stamp, hash;

--Функция пересчета последнего ID (по какой-то причине, иногда, последовательность инкрементируется 2, а не 1 раза)
CREATE OR REPLACE FUNCTION get_prev_id(x_id bigint) RETURNS INT
    LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
BEGIN
    RETURN (SELECT id FROM raw_data WHERE id < x_id ORDER BY id DESC LIMIT 1);
END;
$BODY$;

--Триггерная функция пересчета прошедшего времени на ресурсе
CREATE OR REPLACE FUNCTION update_expected_duration() RETURNS TRIGGER AS $$
DECLARE
    prev_time raw_data.time%TYPE;
    prev_duration raw_data.expected_duration%TYPE;
    prev_hash raw_data.hash%TYPE;
    prev_prev_hash raw_data.hash%TYPE;
    record_count INTEGER;

    prev_id INT;
    prev_prev_id INT;
BEGIN
    prev_id := (select get_prev_id(new.id));
    prev_prev_id := (select get_prev_id(prev_id));

    select hash into prev_hash from raw_data where raw_data.id = prev_id;
    select hash into prev_prev_hash from raw_data where raw_data.id = prev_prev_id;

    SELECT COUNT(*) INTO record_count FROM raw_data;
    IF record_count < 3 OR (new.hash = prev_hash AND prev_hash != prev_prev_hash) THEN
        select time into prev_time from raw_data where raw_data.id = prev_id;
        update raw_data set expected_duration = CURRENT_TIMESTAMP - prev_time where raw_data.id = prev_id;

    elsif prev_hash = new.hash then
        select time into prev_time from raw_data where raw_data.id = prev_id;
        select expected_duration into prev_duration from raw_data where raw_data.id = prev_prev_id;
        update raw_data set expected_duration = prev_duration + (CURRENT_TIMESTAMP - prev_time) where raw_data.id = prev_id;

    elsif prev_hash = prev_prev_hash then
        select time into prev_time from raw_data where raw_data.id = prev_id;
        select expected_duration into prev_duration from raw_data where raw_data.id = prev_prev_id;
        update raw_data set expected_duration = prev_duration + (CURRENT_TIMESTAMP - prev_time) where raw_data.id = prev_id;
    else
        select time into prev_time from raw_data where raw_data.id = prev_id;
        UPDATE raw_data SET expected_duration = CURRENT_TIMESTAMP - prev_time where raw_data.id = prev_id;
    end if;
    RETURN NEW;
END; $$
    LANGUAGE 'plpgsql';

CREATE OR REPLACE TRIGGER update_expected_duration_trigger
    AFTER INSERT ON raw_data
    FOR EACH ROW
EXECUTE PROCEDURE update_expected_duration();
