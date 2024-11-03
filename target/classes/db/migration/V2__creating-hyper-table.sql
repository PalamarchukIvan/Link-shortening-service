CREATE EXTENSION IF NOT EXISTS timescaledb;

SELECT create_hypertable(
               'data', 'time',
                chunk_time_interval => INTERVAL '1 day',
                migrate_data => true
           );

select add_retention_policy('data', INTERVAL '14 day');