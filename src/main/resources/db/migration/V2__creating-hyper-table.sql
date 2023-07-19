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

SELECT create_hypertable(
               'raw_data', 'time',
                chunk_time_interval => INTERVAL '1 minute',
                migrate_data => true
           );

select add_retention_policy('raw_data', INTERVAL '1 minute');