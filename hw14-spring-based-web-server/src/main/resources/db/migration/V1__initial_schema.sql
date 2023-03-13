CREATE TABLE IF NOT EXISTS client
(
     id bigserial not null primary key,
     name character varying(255) COLLATE pg_catalog."default"
);

CREATE TABLE IF NOT EXISTS address
(
    client bigint not null references client (id),
    street character varying(255) COLLATE pg_catalog."default"
);

CREATE TABLE IF NOT EXISTS phone
(
    id bigserial not null primary key,
    "number" character varying(255) COLLATE pg_catalog."default",
    client bigint not null references client (id)
);