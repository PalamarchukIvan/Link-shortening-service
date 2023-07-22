CREATE EXTENSION IF NOT EXISTS timescaledb;
--таблица с данными
create table IF NOT EXISTS raw_data (
                          time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          hash VARCHAR,
                          expected_duration INTERVAL,
                          lag BIGINT,
                          is_found BOOLEAN,
                          PRIMARY KEY(time)
);

SELECT create_hypertable(
               'raw_data', 'time',
                chunk_time_interval => INTERVAL '1 minute',
                migrate_data => true
           );

select add_retention_policy('raw_data', INTERVAL '1 minute');