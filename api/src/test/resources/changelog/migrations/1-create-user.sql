-- liquibase formatted sql

-- changeset asavershin:createUser
CREATE TABLE users
(
    user_id        UUID PRIMARY KEY,
    user_firstname varchar(20)        NOT NULL,
    user_lastname  varchar(20)        NOT NULL,
    user_email     varchar(50) UNIQUE NOT NULL,
    user_password  TEXT               not null
);

CREATE INDEX idx_user_id ON users(user_id);
CREATE INDEX idx_user_email ON users(user_email);