--liquibase formatted sql
CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    name      TEXT NOT NULL,
    age       INT  NOT NULL,
    job_title TEXT
);

CREATE TABLE department_allocations
(
    user_id       BIGINT,
    department_id BIGINT
);