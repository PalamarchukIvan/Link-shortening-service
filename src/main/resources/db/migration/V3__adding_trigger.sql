--Функция пересчета последнего time (по какой-то причине, иногда, последовательность инкрементируется 2, а не 1 раза)
CREATE OR REPLACE FUNCTION get_prev_time(x_time TIMESTAMP WITHOUT TIME ZONE) RETURNS TIMESTAMP WITHOUT TIME ZONE
    LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
BEGIN
    RETURN (SELECT time FROM raw_data WHERE time < x_time ORDER BY time DESC LIMIT 1);
END;
$BODY$;

--Триггерная функция пересчета прошедшего времени на ресурсе
CREATE OR REPLACE FUNCTION update_expected_duration() RETURNS TRIGGER AS $$
DECLARE
    prev_duration raw_data.expected_duration%TYPE;
    prev_hash raw_data.hash%TYPE;
    prev_prev_hash raw_data.hash%TYPE;
    record_count INTEGER;

    prev_time raw_data.time%TYPE;
    prev_prev_time prev_time%TYPE;
BEGIN
    prev_time := (select get_prev_time(new.time));
    prev_prev_time := (select get_prev_time(prev_time));

    select hash into prev_hash from raw_data where raw_data.time = prev_time;
    select hash into prev_prev_hash from raw_data where raw_data.time = prev_prev_time;

    SELECT COUNT(*) INTO record_count FROM raw_data;
    IF record_count < 3 OR (new.hash = prev_hash AND prev_hash != prev_prev_hash) THEN
        update raw_data set expected_duration = CURRENT_TIMESTAMP - prev_time where raw_data.time = prev_time;

    elsif prev_hash = new.hash then
        select expected_duration into prev_duration from raw_data where raw_data.time = prev_prev_time;
        update raw_data set expected_duration = prev_duration + (CURRENT_TIMESTAMP - prev_time) where raw_data.time = prev_time;

    elsif prev_hash = prev_prev_hash then
        select expected_duration into prev_duration from raw_data where raw_data.time = prev_prev_time;
        update raw_data set expected_duration = prev_duration + (CURRENT_TIMESTAMP - prev_time) where raw_data.time = prev_time;
    else
        UPDATE raw_data SET expected_duration = CURRENT_TIMESTAMP - prev_time where raw_data.time = prev_time;
    end if;
    RETURN NEW;
END; $$
    LANGUAGE 'plpgsql';

CREATE OR REPLACE TRIGGER update_expected_duration_trigger
    AFTER INSERT ON raw_data
    FOR EACH ROW
EXECUTE PROCEDURE update_expected_duration();
