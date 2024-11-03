CREATE SEQUENCE users_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE users (
                       id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
                       username VARCHAR(255) UNIQUE NOT NULL,
                       name VARCHAR(255),
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN
);

CREATE TABLE user_roles (
                            user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                            role VARCHAR(50) NOT NULL,
                            PRIMARY KEY (user_id, role)
);
CREATE TABLE short_link (
                            hash VARCHAR(255) PRIMARY KEY,
                            link TEXT NOT NULL,
                            is_deleted BOOLEAN DEFAULT FALSE,
                            user_id BIGINT REFERENCES users(id) ON DELETE SET NULL
);
CREATE INDEX idx_short_link_hashcode ON short_link(hash);

CREATE TABLE data (
                             time TIMESTAMP WITHOUT TIME ZONE PRIMARY KEY,
                             hash VARCHAR(255),
                             user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
                             expected_duration BIGINT,
                             is_found BOOLEAN,
                             lag BIGINT
);
