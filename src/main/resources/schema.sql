CREATE SCHEMA IF NOT EXISTS taaza_khabar;


CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(40) UNIQUE,
    created_at DATE,
    is_verified BOOLEAN
);
ALTER TABLE users
ALTER COLUMN is_verified
SET DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS verification(
    id BIGSERIAL PRIMARY KEY,
    valid_upto TIMESTAMP ,
    verification_code VARCHAR(6),
    user_id BIGSERIAL REFERENCES users(id)
);