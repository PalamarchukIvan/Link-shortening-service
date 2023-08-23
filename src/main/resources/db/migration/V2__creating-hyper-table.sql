CREATE EXTENSION IF NOT EXISTS timescaledb;
--таблица с данными
create table IF NOT EXISTS raw_data_light (
                          time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                          hash VARCHAR,
                          lag BIGINT,
                          is_found BOOLEAN,
                          PRIMARY KEY(time)
);

SELECT create_hypertable(
               'raw_data_light', 'time',
                chunk_time_interval => INTERVAL '1 day',
                migrate_data => true
           );

select add_retention_policy('raw_data_light', INTERVAL '14 day');