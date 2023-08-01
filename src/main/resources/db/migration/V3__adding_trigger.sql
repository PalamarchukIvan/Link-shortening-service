-- Функция пересчета последнего time (по какой-то причине, иногда, последовательность инкрементируется 2, а не 1 раза)
CREATE OR REPLACE FUNCTION get_prev_time(x_time TIMESTAMP WITHOUT TIME ZONE) RETURNS TIMESTAMP WITHOUT TIME ZONE
    LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
BEGIN
    RETURN (SELECT time FROM raw_data WHERE time < x_time ORDER BY time DESC LIMIT 1);
END;
$BODY$;


--Функция поиска последней повторяющейся записи
CREATE OR REPLACE FUNCTION get_prev_time()
    RETURNS TIMESTAMP WITHOUT TIME ZONE AS $$
BEGIN
    RETURN (
        SELECT time
        FROM (
                 SELECT *,
                        LAG(hash) OVER(ORDER BY time) as prev_hash
                 FROM raw_data
             ) sub
        WHERE hash != prev_hash
        ORDER BY time DESC
        LIMIT 1
    );
END;
$$ LANGUAGE 'plpgsql';

--Триггерная функция пересчета прошедшего времени на ресурсе
CREATE OR REPLACE FUNCTION update_expected_duration() RETURNS TRIGGER AS $$
DECLARE
    cur_hash raw_data.hash%TYPE;

    cur_time raw_data.time%TYPE;
    prev_time raw_data.time%TYPE;
    cur_relational_time raw_data.time%TYPE;
BEGIN
    cur_relational_time := (select get_prev_time());

    select hash, time into cur_hash, cur_time from raw_data order by time DESC limit 1;
    if (new.hash = cur_hash) then
        UPDATE raw_data SET expected_duration = CURRENT_TIMESTAMP - cur_relational_time where raw_data.time = cur_time;
    else
        prev_time := (select get_prev_time(cur_time));
        UPDATE raw_data SET expected_duration = CURRENT_TIMESTAMP - prev_time where raw_data.time = cur_time;
    end if;
    RETURN NEW;
END; $$
    LANGUAGE 'plpgsql';

CREATE OR REPLACE TRIGGER update_expected_duration_trigger
    BEFORE INSERT ON raw_data
    FOR EACH ROW
EXECUTE PROCEDURE update_expected_duration();